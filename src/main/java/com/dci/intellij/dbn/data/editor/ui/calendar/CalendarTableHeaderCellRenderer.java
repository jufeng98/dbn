package com.dci.intellij.dbn.data.editor.ui.calendar;

import com.dci.intellij.dbn.common.ui.util.Fonts;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class CalendarTableHeaderCellRenderer extends DefaultTableCellRenderer {
    static final Border EMPTY_BORDER = JBUI.Borders.empty(1, 1, 1, 9);
    static final Color FOREGROUND_COLOR = new JBColor(new Color(67, 123, 203), new Color(67, 123, 203));

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(RIGHT);
        setFont(Fonts.BOLD);
        setBorder(EMPTY_BORDER);
        //setForeground(column == 0 ? Color.RED : GUIUtil.getTableForeground());
        setForeground(FOREGROUND_COLOR);
        return component;
    }
}
