package com.dci.intellij.dbn.execution.statement.result.ui;

import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.execution.ExecutionManager;
import com.dci.intellij.dbn.execution.statement.result.StatementExecutionResult;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class RenameExecutionResultDialog extends DBNDialog<RenameExecutionResultForm> {
    private StatementExecutionResult executionResult;

    public RenameExecutionResultDialog(StatementExecutionResult executionResult) {
        super(executionResult.getProject(), "Rename result", true);
        this.executionResult = executionResult;
        renameAction(getOKAction(), "Rename");
        init();
    }

    @NotNull
    @Override
    protected RenameExecutionResultForm createForm() {
        return new RenameExecutionResultForm(this, executionResult);
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
            getOKAction(),
            getCancelAction()
        };
    }

    @Override
    protected void doOKAction() {
        RenameExecutionResultForm component = getForm();

        boolean stickyResultName = component.isStickyResultName();
        String resultName = component.getResultName();

        executionResult.setName(resultName, stickyResultName);

        ExecutionManager executionManager = ExecutionManager.getInstance(ensureProject());
        executionManager.setRetainStickyNames(stickyResultName);

        super.doOKAction();
    }

    @Override
    @NotNull
    public Action getOKAction() {
        return super.getOKAction();
    }


    @Override
    public void disposeInner() {
        executionResult = null;
        super.disposeInner();
    }
}
