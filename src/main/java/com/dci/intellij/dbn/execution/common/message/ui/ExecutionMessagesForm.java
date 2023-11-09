package com.dci.intellij.dbn.execution.common.message.ui;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.navigation.NavigationInstructions;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.misc.DBNScrollPane;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.common.util.Safe;
import com.dci.intellij.dbn.connection.config.ConnectionConfigListener;
import com.dci.intellij.dbn.execution.common.message.action.*;
import com.dci.intellij.dbn.execution.common.message.ui.tree.MessagesTree;
import com.dci.intellij.dbn.execution.common.ui.ExecutionConsoleForm;
import com.dci.intellij.dbn.execution.compiler.CompilerMessage;
import com.dci.intellij.dbn.execution.explain.result.ExplainPlanMessage;
import com.dci.intellij.dbn.execution.statement.StatementExecutionMessage;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreePath;

import static com.dci.intellij.dbn.common.ui.util.Borderless.markBorderless;

public class ExecutionMessagesForm extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel statusPanel;
    private JPanel messagesPanel;
    private DBNScrollPane messagesScrollPane;

    private final MessagesTree messagesTree;

    public ExecutionMessagesForm(ExecutionConsoleForm parent) {
        super(parent);
        messagesPanel.setBorder(Borders.lineBorder(JBColor.border(), 0, 1, 1, 0));
        messagesTree = new MessagesTree(this);
        markBorderless(messagesTree);

        messagesScrollPane.setViewportView(messagesTree);
        ActionToolbar actionToolbar = Actions.createActionToolbar(
                actionsPanel,
                "DBNavigator.ExecutionMessages.Controls", false,
                new MessagesWindowCloseAction(messagesTree),
                new ExecutedStatementViewAction(messagesTree),
                new MessagesTreeExpandAction(messagesTree),
                new MessagesTreeCollapseAction(messagesTree),
                Actions.SEPARATOR,
                new ExecutionEngineSettingsAction(messagesTree));
        actionsPanel.add(actionToolbar.getComponent());

        ProjectEvents.subscribe(
                ConnectionConfigListener.TOPIC,
                ConnectionConfigListener.whenRemoved(id -> Safe.run(messagesTree, t -> t.removeMessages(id))));
    }

    public void resetMessagesStatus() {
        getMessagesTree().resetMessagesStatus();
    }

    public TreePath addExecutionMessage(StatementExecutionMessage executionMessage, NavigationInstructions instructions) {
        return getMessagesTree().addExecutionMessage(executionMessage, instructions);
    }

    public TreePath addCompilerMessage(CompilerMessage compilerMessage, NavigationInstructions instructions) {
        return getMessagesTree().addCompilerMessage(compilerMessage, instructions);
    }

    public TreePath addExplainPlanMessage(ExplainPlanMessage explainPlanMessage, NavigationInstructions instructions) {
        return getMessagesTree().addExplainPlanMessage(explainPlanMessage, instructions);
    }

    public void reset() {
        getMessagesTree().reset();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public void selectMessage(@NotNull CompilerMessage compilerMessage, NavigationInstructions instructions) {
        getMessagesTree().selectCompilerMessage(compilerMessage, instructions);
    }

    public void selectMessage(@NotNull StatementExecutionMessage statementExecutionMessage, NavigationInstructions instructions) {
        getMessagesTree().selectExecutionMessage(statementExecutionMessage, instructions);
    }

    public void expand(TreePath treePath) {
        getMessagesTree().makeVisible(treePath);
    }

    @NotNull
    public MessagesTree getMessagesTree() {
        return Failsafe.nn(messagesTree);
    }
}
