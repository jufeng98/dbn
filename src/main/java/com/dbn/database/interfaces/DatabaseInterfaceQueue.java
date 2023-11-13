package com.dbn.database.interfaces;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.routine.ThrowableCallable;
import com.dbn.common.routine.ThrowableRunnable;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.interfaces.queue.InterfaceCounters;
import com.dbn.database.interfaces.queue.InterfaceTaskRequest;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public interface DatabaseInterfaceQueue extends StatefulDisposable {
    @NotNull
    ConnectionHandler getConnection();

    int size();

    int maxActiveTasks();

    InterfaceCounters counters();

    <R> R scheduleAndReturn(InterfaceTaskRequest request, ThrowableCallable<R, SQLException> callable) throws SQLException;

    void scheduleAndWait(InterfaceTaskRequest request, ThrowableRunnable<SQLException> runnable) throws SQLException;

    void scheduleAndForget(InterfaceTaskRequest request, ThrowableRunnable<SQLException> runnable) throws SQLException;
}
