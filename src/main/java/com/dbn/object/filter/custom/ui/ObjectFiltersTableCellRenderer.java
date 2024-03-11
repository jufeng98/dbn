package com.dbn.object.filter.custom.ui;

import com.dbn.code.sql.color.SQLTextAttributesKeys;
import com.dbn.common.ui.table.DBNColoredTableCellRenderer;
import com.dbn.common.ui.table.DBNTable;
import com.dbn.common.util.Strings;
import com.dbn.common.util.TextAttributes;
import com.dbn.data.grid.color.DataGridTextAttributesKeys;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.ui.SimpleTextAttributes;

import static com.dbn.common.util.TextAttributes.getSimpleTextAttributes;

public class ObjectFiltersTableCellRenderer extends DBNColoredTableCellRenderer {
    private final SimpleTextAttributes keywordAttributes = getSimpleTextAttributes(SQLTextAttributesKeys.KEYWORD);
    private final SimpleTextAttributes literalAttributes = getSimpleTextAttributes(SQLTextAttributesKeys.STRING);
    private final SimpleTextAttributes numericAttributes = getSimpleTextAttributes(SQLTextAttributesKeys.NUMBER);

    @Override
    protected void customizeCellRenderer(DBNTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
        if (column == 0) {
            DBObjectType objectType = (DBObjectType) value;
            append(objectType.name());
            setIcon(objectType.getIcon());
        } else if (column == 1) {
            String[] tokens = value.toString().split("\\s+");
            for (String token : tokens) {
                if (Strings.isOneOfIgnoreCase(token, "AND", "OR", "IS", "IN", "NOT", "NULL", "LIKE")) {
                    append(token, keywordAttributes);
                } else if (Strings.isQuotedString(token)) {
                    append(token, literalAttributes);
                } else if (Strings.isNumber(token)) {
                    append(token, numericAttributes);
                } else {
                    append(token);
                }
                append(" ");

            }
        }

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
