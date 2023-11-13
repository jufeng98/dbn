package com.dbn.common.option.ui;

import com.dbn.common.option.InteractiveOption;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class InteractiveOptionComboBoxRenderer extends ColoredListCellRenderer<InteractiveOption>{
    public static final InteractiveOptionComboBoxRenderer INSTANCE = new InteractiveOptionComboBoxRenderer();

    private InteractiveOptionComboBoxRenderer() {}

    @Override
    protected void customizeCellRenderer(@NotNull JList list, InteractiveOption value, int index, boolean selected, boolean hasFocus) {
        if (value != null) {
            setIcon(value.getIcon());
            append(value.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
    }
}
