package com.dci.intellij.dbn.execution.method.result.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.method.MethodExecutionContext;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dci.intellij.dbn.execution.ExecutionStatus.*;

public class MethodExecutionStopAction extends AbstractMethodExecutionResultAction {
    public MethodExecutionStopAction() {
        super("Stop Execution", Icons.METHOD_EXECUTION_STOP);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull MethodExecutionResult target) {

        MethodExecutionContext context = target.getExecutionContext();
        context.set(CANCELLED, true);
        context.set(CANCEL_REQUESTED, true);
        ProgressIndicator progress = context.getProgress();
        if (progress != null && !progress.isCanceled()) progress.cancel();

    }

    @Override
    protected void update(
            @NotNull AnActionEvent e,
            @NotNull Presentation presentation,
            @NotNull Project project,
            @Nullable MethodExecutionResult target) {

        presentation.setEnabled(
                target != null &&
                        !target.getDebuggerType().isDebug() &&
                        target.getExecutionContext().is(EXECUTING) &&
                        target.getExecutionContext().isNot(CANCELLED) &&
                        target.getExecutionContext().isNot(CANCEL_REQUESTED)) ;
    }
}