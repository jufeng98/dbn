package com.dci.intellij.dbn.execution.method.result.ui;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.ui.form.DBNForm;
import com.dci.intellij.dbn.common.ui.tab.TabbedPane;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.ui.util.UserInterface;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.SessionId;
import com.dci.intellij.dbn.database.interfaces.DatabaseCompatibilityInterface;
import com.dci.intellij.dbn.execution.common.result.ui.ExecutionResultFormBase;
import com.dci.intellij.dbn.execution.logging.LogOutput;
import com.dci.intellij.dbn.execution.logging.LogOutputContext;
import com.dci.intellij.dbn.execution.logging.ui.DatabaseLoggingResultConsole;
import com.dci.intellij.dbn.execution.method.ArgumentValue;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.DBMethod;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.dci.intellij.dbn.common.util.Commons.nvl;

public class MethodExecutionResultForm extends ExecutionResultFormBase<MethodExecutionResult> {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel statusPanel;
    private JLabel connectionLabel;
    private JLabel durationLabel;
    private JPanel outputCursorsPanel;
    private JTree argumentValuesTree;
    private JPanel argumentValuesPanel;
    private JPanel executionResultPanel;
    private JBScrollPane argumentValuesScrollPane;

    private final TabbedPane outputTabs;


    public MethodExecutionResultForm(@NotNull MethodExecutionResult executionResult) {
        super(executionResult);
        List<ArgumentValue> inputArgumentValues = executionResult.getExecutionInput().getArgumentValues();
        List<ArgumentValue> outputArgumentValues = executionResult.getArgumentValues();
        argumentValuesTree = new ArgumentValuesTree(this, inputArgumentValues, outputArgumentValues);
        argumentValuesScrollPane.setViewportView(argumentValuesTree);


        outputTabs = new TabbedPane(this);
        createActionsPanel();
        updateOutputTabs();

        outputCursorsPanel.add(outputTabs, BorderLayout.CENTER);

        argumentValuesPanel.setBorder(Borders.lineBorder(JBColor.border(), 0, 1, 1, 0));
        updateStatusBarLabels();
        executionResultPanel.setSize(800, -1);
        TreeUtil.expand(argumentValuesTree, 2);
    }

    public DBMethod getMethod() {
        MethodExecutionResult executionResult = getExecutionResult();
        return executionResult.getMethod();
    }

    public void rebuildForm() {
        Dispatch.run(() -> {
            updateArgumentValueTree();
            updateOutputTabs();
            updateStatusBarLabels();
        });
    }

    private void updateArgumentValueTree() {
        MethodExecutionResult executionResult = getExecutionResult();
        List<ArgumentValue> inputArgumentValues = executionResult.getExecutionInput().getArgumentValues();
        List<ArgumentValue> outputArgumentValues = executionResult.getArgumentValues();

        DBMethod method = executionResult.getMethod();
        ArgumentValuesTreeModel treeModel = new ArgumentValuesTreeModel(method, inputArgumentValues, outputArgumentValues);
        argumentValuesTree.setModel(treeModel);
        TreeUtil.expand(argumentValuesTree, 2);
    }

    private void updateOutputTabs() {
        outputTabs.removeAllTabs();
        MethodExecutionResult executionResult = getExecutionResult();
        addOutputArgumentTabs(executionResult);
        addLoggingConsoleTab(executionResult);
        UserInterface.repaint(outputTabs);
    }

    private void addLoggingConsoleTab(MethodExecutionResult executionResult) {
        ConnectionHandler connection = executionResult.getConnection();
        DatabaseCompatibilityInterface compatibility = connection.getCompatibilityInterface();
        String logConsoleName = nvl(compatibility.getDatabaseLogName(), "Output");

        DatabaseLoggingResultConsole console = new DatabaseLoggingResultConsole(connection, logConsoleName, true);
        console.setBorder(Borders.lineBorder(JBColor.border(), 0, 0, 1, 0));

        LogOutputContext context = new LogOutputContext(connection);
        console.writeToConsole(context,
                LogOutput.createSysOutput(context,
                        executionResult.getExecutionContext().getExecutionTimestamp(),
                        " - Method execution started", true));

        String logOutput = executionResult.getLogOutput();
        if (Strings.isNotEmptyOrSpaces(logOutput)) {
            console.writeToConsole(context, LogOutput.createStdOutput(logOutput));
        }
        console.writeToConsole(context, LogOutput.createSysOutput(context, " - Method execution finished\n\n", false));
        Disposer.register(this, console);

        TabInfo outputTabInfo = new TabInfo(console.getComponent());
        outputTabInfo.setText(console.getTitle());
        outputTabInfo.setIcon(Icons.EXEC_LOG_OUTPUT_CONSOLE);
        outputTabInfo.setObject(console);
        outputTabs.addTab(outputTabInfo);
    }

    private void addOutputArgumentTabs(MethodExecutionResult executionResult) {
        List<ArgumentValue> argumentValues = executionResult.getArgumentValues();
        for (ArgumentValue argumentValue : argumentValues) {
            DBArgument argument = argumentValue.getArgument();
            if (argument == null) continue;

            if (argumentValue.isCursor()) {
                DBNForm argumentForm = new MethodExecutionCursorResultForm(this, executionResult, argument);
                addOutputTab(argument, argumentForm);

            } else if (argumentValue.isLargeObject() || argumentValue.isLargeValue()) {
                DBNForm argumentForm = new MethodExecutionLargeValueResultForm(this, argument, argumentValue);
                addOutputTab(argument, argumentForm);
            }
        }
    }

    private void addOutputTab(DBArgument argument, DBNForm form) {
        boolean select = outputTabs.getTabCount() == 0;

        TabInfo tabInfo = new TabInfo(form.getComponent());
        tabInfo.setText(argument.getName());
        tabInfo.setIcon(argument.getIcon());
        tabInfo.setObject(form);
        outputTabs.addTab(tabInfo);

        if (select) outputTabs.select(tabInfo, false);
    }

    void selectArgumentOutputTab(DBArgument argument) {
        for (TabInfo tabInfo : outputTabs.getTabs()) {
            Object object = tabInfo.getObject();
            if (object instanceof MethodExecutionCursorResultForm) {
                MethodExecutionCursorResultForm cursorResultForm = (MethodExecutionCursorResultForm) object;
                if (cursorResultForm.getArgument().equals(argument)) {
                    outputTabs.select(tabInfo, true);
                    break;
                }
            } else if (object instanceof MethodExecutionLargeValueResultForm) {
                MethodExecutionLargeValueResultForm largeValueResultForm = (MethodExecutionLargeValueResultForm) object;
                if (largeValueResultForm.getArgument().equals(argument)) {
                    outputTabs.select(tabInfo, true);
                    break;
                }
            }


        }
    }

    private void updateStatusBarLabels() {
        MethodExecutionResult executionResult = getExecutionResult();
        SessionId sessionId = executionResult.getExecutionInput().getTargetSessionId();
        String connectionType =
                sessionId == SessionId.MAIN ? " (main)" :
                sessionId == SessionId.POOL ? " (pool)" : " (session)";
        ConnectionHandler connection = executionResult.getConnection();
        connectionLabel.setIcon(connection.getIcon());
        connectionLabel.setText(connection.getName() + connectionType);

        durationLabel.setText(": " + executionResult.getExecutionDuration() + " ms");
    }



    private void createActionsPanel() {
        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel, "DBNavigator.ActionGroup.MethodExecutionResult", "DBNavigator.MethodExecutionResult.Controls", false);
        actionsPanel.add(actionToolbar.getComponent());
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

}
