package com.dci.intellij.dbn.execution.method.result.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.data.grid.ui.table.resultSet.ResultSetTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;

public class CursorResultViewRecordAction extends MethodExecutionCursorResultAction {
    public CursorResultViewRecordAction() {
        super("View Record", Icons.EXEC_RESULT_VIEW_RECORD);
    }

    @Override
    public void actionPerformed(AnActionEvent e){
        ResultSetTable resultSetTable = getResultSetTable(e);
        if (resultSetTable != null) {
            resultSetTable.showRecordViewDialog();
        }
    }

    @Override
    public void update(AnActionEvent e) {
        ResultSetTable resultSetTable = getResultSetTable(e);
        boolean enabled = resultSetTable != null && resultSetTable.getSelectedColumn() > -1;
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(enabled);
        presentation.setText("View Record");
    }
}
