package com.dci.intellij.dbn.execution.common.message.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.common.message.ui.tree.MessagesTree;
import com.dci.intellij.dbn.execution.common.message.ui.tree.node.StatementExecutionMessageNode;
import com.dci.intellij.dbn.execution.common.ui.StatementViewerPopup;
import com.dci.intellij.dbn.execution.statement.result.StatementExecutionResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static com.dci.intellij.dbn.common.dispose.Checks.isValid;

public class ExecutedStatementViewAction extends AbstractExecutionMessagesAction {
    public ExecutedStatementViewAction(MessagesTree messagesTree) {
        super(messagesTree, "View SQL statement", Icons.EXEC_RESULT_VIEW_STATEMENT);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull MessagesTree messagesTree) {

        messagesTree.grabFocus();
        StatementExecutionMessageNode execMessageNode =
                (StatementExecutionMessageNode) messagesTree.getSelectionPath().getLastPathComponent();

        StatementExecutionResult executionResult = execMessageNode.getMessage().getExecutionResult();
        StatementViewerPopup statementViewer = new StatementViewerPopup(executionResult);
        statementViewer.show((Component) e.getInputEvent().getSource());
    }

    @Override
    protected void update(
            @NotNull AnActionEvent e,
            @NotNull Presentation presentation,
            @NotNull Project project,
            @Nullable MessagesTree target) {

        boolean enabled =
                isValid(target) &&
                target.getSelectionPath() != null &&
                target.getSelectionPath().getLastPathComponent() instanceof StatementExecutionMessageNode;
        presentation.setEnabled(enabled);
    }
}