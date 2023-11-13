package com.dbn.connection.ui;

import com.dbn.object.common.DBObject;
import com.intellij.ui.DottedBorder;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

public class ObjectListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        DBObject object = (DBObject) value;
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus );
        label.setIcon(object.getIcon(0));
        label.setText(object.getName());
        if (!cellHasFocus && isSelected) {
            label.setForeground(list.getForeground());
            label.setBackground(list.hasFocus() ? list.getBackground() : UIUtil.getFocusedFillColor());
            label.setBorder(new DottedBorder(JBColor.BLACK));
        }
        return label;
    }
}
