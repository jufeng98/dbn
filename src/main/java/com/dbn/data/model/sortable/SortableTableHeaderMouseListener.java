package com.dbn.data.model.sortable;

import com.dbn.common.ref.WeakRef;
import com.dbn.data.sorting.SortDirection;
import com.dbn.data.grid.ui.table.sortable.SortableTable;

import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SortableTableHeaderMouseListener extends MouseAdapter {
    private final WeakRef<SortableTable> table;

    public SortableTableHeaderMouseListener(SortableTable table) {
        this.table = WeakRef.of(table);
    }

    public SortableTable getTable() {
        return table.ensure();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        SortableTable table = getTable();
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point mousePoint = e.getPoint();
            mousePoint.setLocation(mousePoint.getX() - 4, mousePoint.getX());
            JTableHeader tableHeader = table.getTableHeader();
            int columnIndex = tableHeader.columnAtPoint(mousePoint);
            if (columnIndex > -1) {
                Rectangle colRect = tableHeader.getHeaderRect(columnIndex);
                boolean isEdgeClick = colRect.getMaxX() - 8 < mousePoint.getX();
                if (isEdgeClick) {
                    if (e.getClickCount() == 2) {
                        table.accommodateColumnSize(columnIndex, table.getColumnWidthBuffer());
                    }
                } else {
                    boolean keepExisting = e.isControlDown();
                    table.sort(columnIndex, SortDirection.INDEFINITE, keepExisting);
                }
            }
        }
        table.requestFocus();
        //event.consume();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }
}
