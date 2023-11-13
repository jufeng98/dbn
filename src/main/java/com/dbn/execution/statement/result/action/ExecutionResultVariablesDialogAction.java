package com.dbn.execution.statement.result.action;

import com.dbn.common.icon.Icons;
import com.dbn.common.notification.NotificationGroup;
import com.dbn.common.notification.NotificationSupport;
import com.dbn.common.thread.Progress;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.statement.processor.StatementExecutionCursorProcessor;
import com.dbn.execution.statement.variables.StatementExecutionVariablesBundle;
import com.dbn.execution.statement.StatementExecutionManager;
import com.dbn.execution.statement.result.StatementExecutionCursorResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

import static com.dbn.common.dispose.Checks.isValid;
import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class ExecutionResultVariablesDialogAction extends AbstractExecutionResultAction {
    public ExecutionResultVariablesDialogAction() {
        super("Open Variables Dialog", Icons.EXEC_RESULT_OPEN_EXEC_DIALOG);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull StatementExecutionCursorResult executionResult) {
        StatementExecutionCursorProcessor executionProcessor = executionResult.getExecutionProcessor();
        StatementExecutionManager statementExecutionManager = StatementExecutionManager.getInstance(project);
        String statementName = executionResult.getExecutionProcessor().getStatementName();
        statementExecutionManager.promptExecutionDialog(
                executionProcessor,
                DBDebuggerType.NONE,
                () -> Progress.prompt(project, executionProcessor, true,
                        "Executing statement",
                        "Executing " + statementName,
                        progress -> {
                            try {
                                executionProcessor.execute();
                            } catch (SQLException ex) {
                                conditionallyLog(ex);
                                NotificationSupport.sendErrorNotification(
                                        project,
                                        NotificationGroup.EXECUTION,
                                        "Error executing statement. {0}", ex);
                            }
                        }));
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable StatementExecutionCursorResult executionResult) {
        boolean visible = false;
        if (isValid(executionResult)) {
            StatementExecutionCursorProcessor executionProcessor = executionResult.getExecutionProcessor();
            if (isValid(executionProcessor)) {
                StatementExecutionVariablesBundle executionVariables = executionProcessor.getExecutionVariables();
                visible = executionVariables != null && executionVariables.getVariables().size() > 0;
            }
        }
        presentation.setVisible(visible);
        presentation.setText("Open Variables Dialog");
    }
}
