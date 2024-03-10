package com.dbn.object.filter.custom.ui;

import com.dbn.common.ui.component.DBNComponent;
import com.dbn.common.ui.table.DBNEditableTable;
import com.dbn.common.ui.util.Mouse;
import com.dbn.object.filter.custom.ObjectFilterSettings;
import com.dbn.object.type.DBObjectType;
import com.intellij.ui.BooleanTableCellEditor;
import com.intellij.ui.BooleanTableCellRenderer;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ObjectFiltersTable extends DBNEditableTable<ObjectFiltersTableModel> {

    ObjectFiltersTable(DBNComponent parent, ObjectFilterSettings settings) {
        super(parent, createModel(settings), true);
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setSelectionBackground(UIUtil.getTableBackground());
        setSelectionForeground(UIUtil.getTableForeground());
        setCellSelectionEnabled(true);
        setDefaultRenderer(String.class, new ObjectFiltersTableCellRenderer());
        setDefaultRenderer(DBObjectType.class, new ObjectFiltersTableCellRenderer());
        setDefaultRenderer(Boolean.class, new BooleanTableCellRenderer());
        setDefaultEditor(Boolean.class, new BooleanTableCellEditor());

        setFixedWidth(columnModel.getColumn(0), 120);
        setFixedWidth(columnModel.getColumn(2), 80);

        addMouseListener(mouseListener);
    }

    @NotNull
    private static ObjectFiltersTableModel createModel(ObjectFilterSettings filterSettings) {
        return new ObjectFiltersTableModel(filterSettings);
    }

    private void setFixedWidth(TableColumn tableColumn, int width) {
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }

    private final MouseListener mouseListener = Mouse.listener().onClick(e -> {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
            Point point = e.getPoint();
            int columnIndex = columnAtPoint(point);
            if (columnIndex == 2) {
                int rowIndex = rowAtPoint(point);
            }
        }
    });
    
    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        Object value = getValueAtMouseLocation();
        if (value instanceof Color) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2;
    }
}
