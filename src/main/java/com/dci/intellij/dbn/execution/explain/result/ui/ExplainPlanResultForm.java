package com.dci.intellij.dbn.execution.explain.result.ui;

import com.dci.intellij.dbn.common.ui.misc.DBNScrollPane;
import com.dci.intellij.dbn.common.ui.tree.Trees;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.execution.ExecutionManager;
import com.dci.intellij.dbn.execution.ExecutionResult;
import com.dci.intellij.dbn.execution.common.result.ui.ExecutionResultFormBase;
import com.dci.intellij.dbn.execution.explain.result.ExplainPlanResult;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ExplainPlanResultForm extends ExecutionResultFormBase<ExplainPlanResult> {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel resultPanel;
    private DBNScrollPane resultScrollPane;

    private final ExplainPlanTreeTable explainPlanTreeTable;

    public ExplainPlanResultForm(@NotNull ExplainPlanResult explainPlanResult) {
        super(explainPlanResult);
        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel, "DBNavigator.ActionGroup.ExplainPlanResult", "", false);

        actionsPanel.add(actionToolbar.getComponent());

        resultPanel.setBorder(Borders.tableBorder(0,1,0,0));
        ExplainPlanTreeTableModel treeTableModel = new ExplainPlanTreeTableModel(explainPlanResult);
        explainPlanTreeTable = new ExplainPlanTreeTable(this, treeTableModel);

        resultScrollPane.setViewportView(explainPlanTreeTable);
    }

    public ExplainPlanTreeTable getExplainPlanTreeTable() {
        return explainPlanTreeTable;
    }

    public void show() {
        ExecutionResult<?> executionResult = getExecutionResult();
        Project project = executionResult.getProject();
        ExecutionManager executionManager = ExecutionManager.getInstance(project);
        executionManager.selectResultTab(executionResult);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public void collapseAllNodes() {
        Trees.collapseAll(explainPlanTreeTable.getTree());
    }

    public void expandAllNodes() {
        Trees.expandAll(explainPlanTreeTable.getTree());
    }
}
