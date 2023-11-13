package com.dbn.data.export.ui;

import com.dbn.common.thread.Dispatch;
import com.dbn.common.thread.Progress;
import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.data.export.DataExportInstructions;
import com.dbn.data.export.DataExportManager;
import com.dbn.data.grid.ui.table.resultSet.ResultSetTable;
import com.dbn.execution.ExecutionResult;
import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExportDataDialog extends DBNDialog<ExportDataForm> {
    private final ResultSetTable<?> table;
    private final ConnectionRef connection;
    private final DBObjectRef<?> sourceObject;

    public ExportDataDialog(ResultSetTable<?> table, @NotNull DBObject sourceObject) {
        this(table, sourceObject, sourceObject.getConnection());
    }

    public ExportDataDialog(ResultSetTable<?> table, @NotNull ExecutionResult<?> executionResult) {
        this(table, null, executionResult.getConnection());
    }


    private ExportDataDialog(ResultSetTable<?> table, @Nullable DBObject sourceObject, @NotNull ConnectionHandler connection) {
        super(connection.getProject(), "Export data", true);
        this.table = table;
        this.connection = connection.ref();
        this.sourceObject = DBObjectRef.of(sourceObject);
        init();
    }

    @NotNull
    @Override
    protected ExportDataForm createForm() {
        DBObject sourceObject = DBObjectRef.get(this.sourceObject);
        ConnectionHandler connection = this.connection.ensure();
        DataExportManager exportManager = DataExportManager.getInstance(connection.getProject());
        DataExportInstructions instructions = exportManager.getExportInstructions();
        boolean hasSelection = table.getSelectedRowCount() > 1 || table.getSelectedColumnCount() > 1;
        instructions.setBaseName(table.getName());
        return new ExportDataForm(this, instructions, hasSelection, connection, sourceObject);
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{
                new DialogWrapperAction("Export") {
                    @Override
                    protected void doAction(ActionEvent actionEvent) {
                        doOKAction();
                    }
                },
                getCancelAction()};
    }

    @Override
    protected void doOKAction() {
        getForm().validateEntries(
                () -> {
                    Project project = getProject();
                    ConnectionHandler connection = getConnection();
                    DataExportInstructions exportInstructions = getForm().getExportInstructions();
                    Progress.modal(project, connection, true,
                            "Exporting data",
                            "Exporting " + exportInstructions.getFormat() + " data to " + exportInstructions.getDestination(),
                            progress -> {
                                DataExportManager exportManager = DataExportManager.getInstance(project);
                                exportManager.setExportInstructions(exportInstructions);
                                exportManager.exportTableContent(
                                        table,
                                        exportInstructions,
                                        connection,
                                        () -> Dispatch.run(() -> ExportDataDialog.super.doOKAction()));
                            });
                }
        );
    }
}
