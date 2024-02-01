package com.dbn.execution.logging.action;

import com.dbn.common.icon.Icons;
import com.dbn.common.util.Messages;
import com.dbn.execution.ExecutionManager;
import com.dbn.execution.logging.DatabaseLoggingResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.util.Conditional.when;

public class DatabaseLogOutputCloseAction extends AbstractDatabaseLoggingAction {
    public DatabaseLogOutputCloseAction() {
        super("Close", Icons.EXEC_RESULT_CLOSE);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull DatabaseLoggingResult loggingResult) {
        if (loggingResult.getContext().isActive()) {
            Messages.showQuestionDialog(
                    project,
                    "Process active",
                    "The process is still active. Closing the log output will interrupt the process. \nAre you sure you want to close the console?",
                    Messages.OPTIONS_YES_NO, 0,
                    option -> when(option == 0, () -> closeConsole(loggingResult, project)));
        } else {
            closeConsole(loggingResult, project);
        }
    }

    private void closeConsole(DatabaseLoggingResult loggingResult, Project project) {
        loggingResult.getContext().close();
        ExecutionManager executionManager = ExecutionManager.getInstance(project);
        executionManager.removeResultTab(loggingResult);
    }
}
