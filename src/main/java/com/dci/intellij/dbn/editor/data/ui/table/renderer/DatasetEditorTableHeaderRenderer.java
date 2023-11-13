package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.table.DBNTableHeaderRendererBase;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.data.grid.options.DataGridSettings;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.data.sorting.SortingState;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModel;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;

import javax.swing.*;
import java.awt.*;

import static com.dci.intellij.dbn.common.dispose.Failsafe.guarded;

public class DatasetEditorTableHeaderRenderer extends DBNTableHeaderRendererBase {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JLabel sortingLabel;

    public DatasetEditorTableHeaderRenderer() {
        mainPanel.setOpaque(true);
        mainPanel.setBackground(Colors.getPanelBackground());
        mainPanel.setBorder(Borders.tableBorder(0, 0, 0, 1));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
        return guarded(mainPanel, () -> {
            DatasetEditorModel model = (DatasetEditorModel) table.getModel();
            sortingLabel.setText(null);
            int width = 0;
            String columnName = value.toString();
            SortingState sortingState = model.getSortingState();
            SortingInstruction sortingInstruction = sortingState.getSortingInstruction(columnName);

            if (sortingInstruction != null) {
                Icon icon = sortingInstruction.getDirection() == SortDirection.ASCENDING ?
                        Icons.DATA_EDITOR_SORT_ASC :
                        Icons.DATA_EDITOR_SORT_DESC;
                sortingLabel.setIcon(icon);
                width += icon.getIconWidth();
                if (sortingState.size() > 1) {
                    sortingLabel.setText(Integer.toString(sortingInstruction.getIndex()));
                }
            } else {
                sortingLabel.setIcon(null);
            }

            nameLabel.setText(columnName);
            DBDataset dataset = model.getDataset();
            DBColumn column = dataset.getColumn(columnName);
            if (column != null) {
                boolean primaryKey = column.isPrimaryKey();
                boolean foreignKey = column.isForeignKey();
                Icon icon = null;
                if (primaryKey && foreignKey) {
                    icon = Icons.DBO_LABEL_PK_FK;
                } else if (primaryKey) {
                    icon = Icons.DBO_LABEL_PK;
                } else if (foreignKey) {
                    icon = Icons.DBO_LABEL_FK;
                }
                nameLabel.setIcon(icon);
                if (icon != null) {
                    width += icon.getIconWidth();
                }
            }
            nameLabel.setForeground(Colors.getLabelForeground());

            FontMetrics fontMetrics = getFontMetrics();
            width += fontMetrics.stringWidth(columnName) + 20;
            int height = fontMetrics.getHeight() + 6;
            mainPanel.setPreferredSize(new Dimension(width, height));
            mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            updateTooltip(column);
            return mainPanel;
        });
    }

    @Override
    protected JLabel getNameLabel() {
        return nameLabel;
    }

    private void updateTooltip(DBColumn column) {
        if (column != null) {
            DataGridSettings dataGridSettings = DataGridSettings.getInstance(column.getProject());
            if (dataGridSettings.getGeneralSettings().isColumnTooltipEnabled()) {
                String toolTipText = "<b>" + column.getName() + "</b><br>" + column.getDataType().getQualifiedName() + "";

                StringBuilder attributes  = new StringBuilder();
                if (column.isPrimaryKey()) attributes.append("PK");
                if (column.isForeignKey()) attributes.append(" FK");
                if (!column.isPrimaryKey() && !column.isNullable()) attributes.append(" not null");

                if (attributes.length() > 0) {
                    toolTipText += "<br>" + attributes + "";
                }

                mainPanel.setToolTipText(toolTipText);
            } else {
                mainPanel.setToolTipText(null);
            }
        } else {
            mainPanel.setToolTipText(null);
        }
    }
}
