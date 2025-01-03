package com.dbn.common.ui.table;

import com.dbn.common.color.Colors;
import com.dbn.common.ui.component.DBNComponent;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.util.Borders;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.TableUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import java.awt.*;

import static com.dbn.common.dispose.Failsafe.guarded;

public class DBNEditableTable<T extends DBNEditableTableModel> extends DBNTableWithGutter<T> {
    public static final LineBorder SELECTION_BORDER = new LineBorder(Colors.getTableBackground());

    public DBNEditableTable(DBNComponent parent, T model, boolean showHeader) {
        super(parent, model, showHeader);
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        ListSelectionListener selectionListener = e -> {
            if (!e.getValueIsAdjusting() && getSelectedRowCount() == 1) {
                startCellEditing();
            }
        };
        getSelectionModel().addListSelectionListener(selectionListener);
        setSelectionBackground(Colors.getTableBackground());
        setSelectionForeground(Colors.getTableForeground());
        setCellSelectionEnabled(true);
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        setDefaultRenderer(String.class, new DBNColoredTableCellRenderer() {
            @Override
            protected void customizeCellRenderer(DBNTable<?> table, Object value, boolean selected, boolean hasFocus, int row, int column) {
                acquireState(table, false, false, row, column);
                SimpleTextAttributes attributes = SimpleTextAttributes.SIMPLE_CELL_ATTRIBUTES;
                if (selected && !table.isEditing()) {
                    attributes = SimpleTextAttributes.SELECTED_SIMPLE_CELL_ATTRIBUTES;
                }
                append(value == null ? "" : (String) value, attributes);
            }
        });
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
        super.columnSelectionChanged(e);
        JTableHeader tableHeader = getTableHeader();
        if (tableHeader != null && tableHeader.getDraggedColumn() == null) {
            if (!e.getValueIsAdjusting()) {
                startCellEditing();
            }
        }
    }

    private void startCellEditing() {
        if (getModel().getRowCount() > 0) {
            int selectedRow = getSelectedRow();
            int selectedColumn = getSelectedColumn();

            if (selectedRow > -1 && selectedColumn > -1) {
                TableCellEditor cellEditor = getCellEditor();
                if (cellEditor == null) {
                    editCellAt(selectedRow, selectedColumn);
                }
            }
        }
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        guarded(() -> {
            checkDisposed();
            super.editingStopped(e);
            T model = getModel();
            model.notifyListeners(0, model.getRowCount(), 0);
        });
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int rowIndex, int columnIndex) {
        Component component = super.prepareEditor(editor, rowIndex, columnIndex);
        if (component instanceof JTextField textField) {
            textField.setBorder(Borders.EMPTY_BORDER);

            //selectCell(rowIndex, columnIndex);

            Dispatch.run(() -> {
                component.requestFocus();
                textField.selectAll();
            });
        }
        return component;
    }

    public void insertRow() {
        stopCellEditing();
        int rowIndex = getSelectedRow();
        T model = getModel();
        rowIndex = model.getRowCount() == 0 ? 0 : rowIndex + 1;
        model.insertRow(rowIndex);
        resizeAndRepaint();
        getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
    }


    public void removeRow() {
        stopCellEditing();
        int selectedRow = getSelectedRow();
        T model = getModel();
        model.removeRow(selectedRow);
        resizeAndRepaint();

        if (model.getRowCount() == selectedRow && selectedRow > 0) {
            getSelectionModel().setSelectionInterval(selectedRow -1, selectedRow -1);
        }
    }

    public void moveRowUp() {
        int selectedRow = getSelectedRow();
        int selectedColumn = getSelectedColumn();

        TableUtil.moveSelectedItemsUp(this);
        selectCell(selectedRow -1, selectedColumn);
    }

    public void moveRowDown() {
        int selectedRow = getSelectedRow();
        int selectedColumn = getSelectedColumn();

        TableUtil.moveSelectedItemsDown(this);
        selectCell(selectedRow + 1, selectedColumn);
    }
}
