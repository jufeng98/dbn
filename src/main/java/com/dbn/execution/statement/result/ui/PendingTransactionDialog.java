package com.dbn.execution.statement.result.ui;

import com.dbn.common.icon.Icons;
import com.dbn.common.ui.dialog.DialogWithTimeout;
import com.dbn.common.util.TimeUtil;
import com.dbn.connection.Resources;
import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.execution.ExecutionStatus;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PendingTransactionDialog extends DialogWithTimeout {
    private final CommitAction commitAction;
    private final RollbackAction rollbackAction;
    private final StatementExecutionProcessor executionProcessor;
    private final PendingTransactionDialogForm transactionForm;

    public PendingTransactionDialog(StatementExecutionProcessor executionProcessor) {
        super(executionProcessor.getProject(), "Open transactions", true, TimeUtil.getSeconds(5));
        setModal(true);
        setResizable(true);
        this.executionProcessor = executionProcessor;
        commitAction = new CommitAction();
        rollbackAction = new RollbackAction();
        transactionForm = new PendingTransactionDialogForm(this, executionProcessor);
        setModal(false);
        init();
    }

    @Override
    protected JComponent createContentComponent() {
        return transactionForm.getComponent();
    }

    @Override
    public void doDefaultAction() {
        DBNConnection connection = getConnection();
        Resources.rollbackSilently(connection);
    }

    @Override
    protected String getDimensionServiceKey() {
        return null;
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                commitAction,
                rollbackAction,
                getHelpAction()
        };
    }

    private class CommitAction extends AbstractAction {
        CommitAction() {
            super("Commit", Icons.CONNECTION_COMMIT);
            makeDefaultAction(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DBNConnection connection = getConnection();
                Resources.commitSilently(connection);
            } finally {
                executionProcessor.getExecutionContext().set(ExecutionStatus.PROMPTED, false);
                executionProcessor.postExecute();
            }
            doOKAction();
        }
    }

    DBNConnection getConnection() {
        return executionProcessor.getExecutionContext().getConnection();
    }

    private class RollbackAction extends AbstractAction {
        RollbackAction() {
            super("Rollback", Icons.CONNECTION_ROLLBACK);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DBNConnection connection = getConnection();
                Resources.rollbackSilently(connection);
            } finally {
                executionProcessor.getExecutionContext().set(ExecutionStatus.PROMPTED, false);
                executionProcessor.postExecute();
            }
            doOKAction();
        }
    }

    @Override
    public void doCancelAction() {
        DBNConnection connection = getConnection();
        Resources.rollbackSilently(connection);
        super.doCancelAction();
    }
}
