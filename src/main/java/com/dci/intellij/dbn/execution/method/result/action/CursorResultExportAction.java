package com.dci.intellij.dbn.execution.method.result.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Dialogs;
import com.dci.intellij.dbn.data.export.ui.ExportDataDialog;
import com.dci.intellij.dbn.data.grid.ui.table.resultSet.ResultSetTable;
import com.dci.intellij.dbn.object.DBArgument;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class CursorResultExportAction extends MethodExecutionCursorResultAction {
    public CursorResultExportAction() {
        super("Export Data", Icons.DATA_EXPORT);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ResultSetTable<?> resultSetTable = getResultSetTable(e);
        if (resultSetTable == null) return;

        DBArgument methodArgument = getMethodArgument(e);
        if (methodArgument == null) return;

        Dialogs.show(() -> new ExportDataDialog(resultSetTable, methodArgument));
    }
}
