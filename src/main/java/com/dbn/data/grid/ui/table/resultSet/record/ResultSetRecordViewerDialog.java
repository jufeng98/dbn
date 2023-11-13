package com.dbn.data.grid.ui.table.resultSet.record;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.data.grid.ui.table.resultSet.ResultSetTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ResultSetRecordViewerDialog extends DBNDialog<ResultSetRecordViewerForm> {
    private final ResultSetTable<?> table;
    private final boolean showDataTypes;
    public ResultSetRecordViewerDialog(ResultSetTable<?> table, boolean showDataTypes) {
        super(table.getProject(), "View record", true);
        this.table = table;
        this.showDataTypes = showDataTypes;
        setModal(true);
        setResizable(true);
        renameAction(getCancelAction(), "Close");
        init();
    }

    @NotNull
    @Override
    protected ResultSetRecordViewerForm createForm() {
        return new ResultSetRecordViewerForm(this, table, showDataTypes);
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }
}
