package com.dbn.common.ui.listener;

import com.dbn.common.ui.util.UserInterface;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ComboBoxSelectionKeyListener extends KeyAdapter {
    private JComboBox comboBox;
    private boolean useControlKey;

    public static KeyListener create(JComboBox comboBox, boolean useControlKey) {
        return new ComboBoxSelectionKeyListener(comboBox, useControlKey);
    }

    private ComboBoxSelectionKeyListener(JComboBox comboBox, boolean useControlKey) {
        this.comboBox = comboBox;
        this.useControlKey = useControlKey;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!e.isConsumed()) {
            int operatorSelectionIndex = comboBox.getSelectedIndex();
            if ((useControlKey && e.getModifiers() == InputEvent.CTRL_MASK) ||
                    (!useControlKey && e.getModifiers() != InputEvent.CTRL_MASK)) {
                if (e.getKeyCode() == 38) {//UP
                    if (operatorSelectionIndex > 0) {
                        comboBox.setSelectedIndex(operatorSelectionIndex - 1);
                        UserInterface.repaint(comboBox);
                    }
                    e.consume();
                } else if (e.getKeyCode() == 40) { // DOWN
                    if (operatorSelectionIndex < comboBox.getItemCount() - 1) {
                        comboBox.setSelectedIndex(operatorSelectionIndex + 1);
                        UserInterface.repaint(comboBox);
                    }
                    e.consume();
                }
            }
        }
    }
}
