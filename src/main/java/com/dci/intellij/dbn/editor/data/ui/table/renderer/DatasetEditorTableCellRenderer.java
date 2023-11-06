package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.ui.table.DBNTable;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.util.Commons;
import com.dci.intellij.dbn.data.grid.color.BasicTableTextAttributes;
import com.dci.intellij.dbn.data.grid.ui.table.basic.BasicTableCellRenderer;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorColumnInfo;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModelCell;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModelRow;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.border.Border;
import java.awt.*;

import static com.dci.intellij.dbn.common.dispose.Checks.allValid;
import static com.dci.intellij.dbn.editor.data.model.RecordStatus.*;

public class DatasetEditorTableCellRenderer extends BasicTableCellRenderer {

    @Override
    protected void customizeCellRenderer(DBNTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
        acquireState(table, isSelected, false, rowIndex, columnIndex);
        DatasetEditorModelCell cell = (DatasetEditorModelCell) value;
        DatasetEditorTable datasetEditorTable = (DatasetEditorTable) table;

        if (!allValid(cell, datasetEditorTable, datasetEditorTable.getProject())) return;

        DatasetEditorModelRow row = cell.getRow();
        DatasetEditorColumnInfo columnInfo = cell.getColumnInfo();
        boolean dirty = datasetEditorTable.getModel().isDirty();
        boolean loading = datasetEditorTable.isLoading();
        boolean inserting = datasetEditorTable.isInserting();

        boolean modified = cell.is(MODIFIED);
        boolean updating = cell.is(UPDATING);
        boolean deletedRow = row.is(DELETED);
        boolean insertRow = row.is(INSERTING);
        boolean caretRow = !insertRow && table.getCellSelectionEnabled() && table.getSelectedRow() == rowIndex && table.getSelectedRowCount() == 1;
        boolean auditColumn = columnInfo != null && columnInfo.isAuditColumn();
        boolean primaryKey = columnInfo != null && columnInfo.isPrimaryKey();
        boolean foreignKey = columnInfo != null && columnInfo.isForeignKey();
        boolean connected = Failsafe.nn(datasetEditorTable.getDatasetEditor().getConnection()).isConnected();

        BasicTableTextAttributes attributes = (BasicTableTextAttributes) getAttributes();
        SimpleTextAttributes textAttributes = attributes.getPlainData(modified, caretRow);

        if (isSelected) {
            textAttributes = attributes.getSelection();
        } else {
            if (loading || dirty || !connected || updating) {
                textAttributes = attributes.getLoadingData(caretRow);
            } else if (deletedRow) {
                textAttributes = attributes.getDeletedData();
            } else if ((inserting && !insertRow)) {
                textAttributes = attributes.getReadonlyData(modified, caretRow);
            } else if (primaryKey) {
                textAttributes = attributes.getPrimaryKey(modified, caretRow);
            } else if (foreignKey) {
                textAttributes = attributes.getForeignKey(modified, caretRow);
            } else if (cell.isLobValue() || cell.isArrayValue()) {
                textAttributes = attributes.getReadonlyData(modified, caretRow);
            } else if (auditColumn) {
                textAttributes = attributes.getAuditData(modified, caretRow);
            }
        }

        Color background = Commons.nvl(textAttributes.getBgColor(), table.getBackground());
        Color foreground = Commons.nvl(textAttributes.getFgColor(), table.getForeground());


        Border border = Borders.lineBorder(background);

        if (cell.hasError() && connected) {
            SimpleTextAttributes errorData = attributes.getErrorData();
            //border = Borders.lineBorder(SimpleTextAttributes.ERROR_ATTRIBUTES.getFgColor());
            border = Borders.lineBorder(errorData.getBgColor());
            background = errorData.getBgColor();
            foreground = errorData.getFgColor();
            textAttributes = textAttributes.derive(errorData.getStyle(), foreground, background, null);
        } else if (auditColumn && !isSelected) {
            SimpleTextAttributes auditDataAttr = attributes.getAuditData(modified, caretRow);
            foreground = Commons.nvl(auditDataAttr.getFgColor(), foreground);
            textAttributes = textAttributes.derive(textAttributes.getStyle(), foreground, background, null);
        }

        setBorder(border);
        setBackground(background);
        setForeground(foreground);
        writeUserValue(cell, textAttributes, attributes);
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
    }
}
                                                                