package com.dbn.object.filter.custom.ui;

import com.dbn.common.ui.table.DBNColoredTableCellRenderer;
import com.dbn.common.ui.table.DBNTable;

public class ObjectFiltersTableCellRenderer extends DBNColoredTableCellRenderer {
    @Override
    protected void customizeCellRenderer(DBNTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
/*        if (column == 2 || column == 3) {

        }
        else if (column == 4) {
            Color color = (Color) value;
            if (color == null) color = EnvironmentType.EnvironmentColor.NONE;
            setBackground(color);
            setBorder(new CompoundBorder(
                    new LineBorder(table.getBackground()),
                    new ColoredSideBorder(color.brighter(), color.brighter(), color.darker(), color.darker(), 1)));
        } else {
            String stringValue = (String) value;
            if (Strings.isNotEmpty(stringValue)) {
                append(stringValue, SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }
        }*/
    }
}
