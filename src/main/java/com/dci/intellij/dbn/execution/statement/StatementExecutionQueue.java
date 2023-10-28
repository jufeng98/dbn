package com.dci.intellij.dbn.execution.statement;

import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.thread.Progress;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.dci.intellij.dbn.common.dispose.Failsafe.guarded;
import static com.dci.intellij.dbn.execution.ExecutionStatus.*;

public final class StatementExecutionQueue extends StatefulDisposableBase {
    private final Queue<StatementExecutionProcessor> processors = new ConcurrentLinkedQueue<>();
    private final ConnectionRef connection;
    private volatile boolean executing = false;

    public StatementExecutionQueue(ConnectionHandler connection) {
        super(connection);
        this.connection = connection.ref();
    }

    void queue(StatementExecutionProcessor processor) {
        StatementExecutionContext context = processor.getExecutionContext();
        context.set(CANCELLED, false);
        if (!this.processors.contains(processor)) {
            context.set(QUEUED, true);
            this.processors.offer(processor);
            execute();
        }
    }

    @NotNull
    public Project getProject() {
        return getConnection().getProject();
    }

    public ConnectionHandler getConnection() {
        return ConnectionRef.ensure(connection);
    }

    private synchronized void execute() {
        if (executing) return;
        executing = true;

        Project project = getProject();
        ConnectionHandler connection = getConnection();
        Progress.background(project, connection, true,
                "Executing statements",
                "Executing SQL statements",
                progress -> {
                    try {
                        StatementExecutionProcessor processor = processors.poll();
                        while (processor != null) {
                            execute(processor);

                            if (progress.isCanceled()) {
                                cancelExecution();
                            }
                            processor = processors.poll();
                        }
                    } finally {
                        executing = false;
                        if (progress.isCanceled()) {
                            cancelExecution();
                        }
                    }
                });
    }

    private void execute(StatementExecutionProcessor processor) {
        guarded(processor, p -> {
            Project project = getProject();
            StatementExecutionContext context = p.getExecutionContext();
            context.set(QUEUED, false);
            context.set(EXECUTING, true);
            StatementExecutionManager statementExecutionManager = StatementExecutionManager.getInstance(project);
            statementExecutionManager.process(p);
        });
    }

    private void cancelExecution() {
        // cleanup queue for untouched processors
        StatementExecutionProcessor processor = processors.poll();
        while(processor != null) {
            processor.getExecutionContext().reset();
            processor = processors.poll();
        }
    }

    public boolean contains(StatementExecutionProcessor processor) {
        return processors.contains(processor);
    }

    public void cancelExecution(StatementExecutionProcessor processor) {
        processor.getExecutionContext().set(QUEUED, false);
        processors.remove(processor);
        if (processors.size() == 0) {
            executing = false;
        }
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
