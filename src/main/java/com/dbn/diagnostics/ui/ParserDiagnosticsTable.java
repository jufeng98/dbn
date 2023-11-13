package com.dbn.diagnostics.ui;

import com.dbn.common.color.Colors;
import com.dbn.common.ui.component.DBNComponent;
import com.dbn.common.ui.table.DBNColoredTableCellRenderer;
import com.dbn.common.ui.table.DBNTable;
import com.dbn.common.ui.table.DBNTableTransferHandler;
import com.dbn.common.ui.util.Borders;
import com.dbn.common.ui.util.ClientProperty;
import com.dbn.common.ui.util.Mouse;
import com.dbn.common.util.Editors;
import com.dbn.diagnostics.data.DiagnosticEntry;
import com.dbn.diagnostics.data.ParserDiagnosticsEntry;
import com.dbn.diagnostics.data.StateTransition;
import com.dbn.diagnostics.ui.model.ParserDiagnosticsTableModel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.MouseEvent;

public class ParserDiagnosticsTable extends DBNTable<ParserDiagnosticsTableModel> {

    public ParserDiagnosticsTable(@NotNull DBNComponent parent, ParserDiagnosticsTableModel model) {
        super(parent, model, true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDefaultRenderer(DiagnosticEntry.class, new CellRenderer());
        setTransferHandler(DBNTableTransferHandler.INSTANCE);
        setBackground(Colors.getEditorBackground());
        initTableSorter();
        setCellSelectionEnabled(true);
        adjustRowHeight(2);
        accommodateColumnsSize();
        addMouseListener(Mouse.listener().onClick(e -> clickEvent(e)));
        ClientProperty.BORDER.set(this, Borders.tableBorder(1, 0, 0, 0));

    }

    private void clickEvent(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() != 2) return;

        int selectedRow = getSelectedRow();
        if (selectedRow <= -1) return;

        ParserDiagnosticsEntry entry = (ParserDiagnosticsEntry) getValueAt(selectedRow, 0);
        if (entry == null) return;

        VirtualFile virtualFile = entry.getFile();
        if (virtualFile != null) {
            Editors.openFileEditor(getProject(), virtualFile, true);
        }
    }

    @Override
    protected int getMaxColumnWidth() {
        return 800;
    }

    @Override
    public void setModel(@NotNull TableModel dataModel) {
        super.setModel(dataModel);
        initTableSorter();
    }

    private static class CellRenderer extends DBNColoredTableCellRenderer {
        @Override
        protected void customizeCellRenderer(DBNTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
            ParserDiagnosticsEntry entry = (ParserDiagnosticsEntry) value;
            ParserDiagnosticsTableModel model = (ParserDiagnosticsTableModel) table.getModel();
            Object columnValue = model.getValue(entry, column);

            SimpleTextAttributes textAttributes = SimpleTextAttributes.REGULAR_ATTRIBUTES;
            if (columnValue instanceof StateTransition) {
                StateTransition stateTransition = (StateTransition) columnValue;
                textAttributes = stateTransition.getCategory().getTextAttributes();
            } else if (columnValue instanceof VirtualFile) {
                VirtualFile virtualFile = (VirtualFile) columnValue;
                setIcon(virtualFile.getFileType().getIcon());
            }

            String presentableValue = model.getPresentableValue(entry, column);
            append(presentableValue, textAttributes);
            setBorder(Borders.TEXT_FIELD_INSETS);
        }
    }
}
