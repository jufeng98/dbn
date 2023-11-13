package com.dbn.editor.data.ui.table.listener;

import com.dbn.common.ref.WeakRef;
import com.dbn.data.model.ColumnInfo;
import com.dbn.editor.data.ui.table.DatasetEditorTable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DatasetEditorHeaderMouseListener extends MouseAdapter {
    private final WeakRef<DatasetEditorTable> table;

    public DatasetEditorHeaderMouseListener(DatasetEditorTable table) {
        this.table = WeakRef.of(table);
    }

    @NotNull
    public DatasetEditorTable getTable() {
        return table.ensure();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            DatasetEditorTable table = getTable();
            Point mousePoint = e.getPoint();
            int tableColumnIndex = table.getTableHeader().columnAtPoint(mousePoint);
            if (tableColumnIndex > -1) {
                int modelColumnIndex = table.convertColumnIndexToModel(tableColumnIndex);
                if (modelColumnIndex > -1) {
                    ColumnInfo columnInfo = table.getModel().getColumnInfo(modelColumnIndex);
                    table.showPopupMenu(e, null, columnInfo);
                }
            }
        }
    }
}
