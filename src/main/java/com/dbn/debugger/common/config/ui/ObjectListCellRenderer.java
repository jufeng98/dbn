package com.dbn.debugger.common.config.ui;

import com.dbn.object.common.DBObject;

import javax.swing.*;
import java.awt.*;

public class ObjectListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        DBObject object = (DBObject) value;
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus );
        label.setIcon(object.getIcon(0));
        label.setText(object.getQualifiedName());
        return label;
    }
}