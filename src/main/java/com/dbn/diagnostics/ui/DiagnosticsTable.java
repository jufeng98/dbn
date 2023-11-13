package com.dbn.diagnostics.ui;

import com.dbn.common.color.Colors;
import com.dbn.common.ui.Presentable;
import com.dbn.common.ui.component.DBNComponent;
import com.dbn.common.ui.table.DBNColoredTableCellRenderer;
import com.dbn.common.ui.table.DBNTable;
import com.dbn.common.ui.table.DBNTableTransferHandler;
import com.dbn.common.ui.util.Borderless;
import com.dbn.common.ui.util.Borders;
import com.dbn.diagnostics.data.DiagnosticEntry;
import com.dbn.diagnostics.ui.model.AbstractDiagnosticsTableModel;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableModel;

public class DiagnosticsTable<T extends AbstractDiagnosticsTableModel> extends DBNTable<T> implements Borderless{

    DiagnosticsTable(@NotNull DBNComponent parent, T model) {
        super(parent, model, true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDefaultRenderer(DiagnosticEntry.class, new CellRenderer());
        setTransferHandler(DBNTableTransferHandler.INSTANCE);
        setBackground(Colors.getEditorBackground());
        setCellSelectionEnabled(true);
        adjustRowHeight(2);
        initTableSorter();
        accommodateColumnsSize();
    }

    @Override
    public void setModel(@NotNull TableModel dataModel) {
        super.setModel(dataModel);
        initTableSorter();
    }

    private class CellRenderer extends DBNColoredTableCellRenderer {
        @Override
        protected void customizeCellRenderer(DBNTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
            DiagnosticEntry<?> entry = (DiagnosticEntry) value;
            T model = getModel();
            Object columnValue = model.getValue(entry, column);
            if (columnValue instanceof Presentable) {
                Presentable presentable = (Presentable) columnValue;
                setIcon(presentable.getIcon());
                append(presentable.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            } else {
                String presentableValue = model.getPresentableValue(entry, column);
                append(presentableValue, SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }
            setBorder(Borders.TEXT_FIELD_INSETS);
        }
    }
}
