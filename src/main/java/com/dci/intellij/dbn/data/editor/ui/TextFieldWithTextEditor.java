package com.dci.intellij.dbn.data.editor.ui;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.ui.misc.DBNButton;
import com.dci.intellij.dbn.common.ui.util.Keyboard;
import com.dci.intellij.dbn.common.ui.util.Mouse;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.data.editor.text.ui.TextEditorDialog;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextFieldWithTextEditor extends TextFieldWithButtons {
    private final DBNButton button;
    private final String displayValue;

    public TextFieldWithTextEditor(@NotNull Project project) {
        this(project, null);
    }

    public TextFieldWithTextEditor(@NotNull Project project, String displayValue) {
        super(project);
        this.displayValue = displayValue;
        setBounds(0, 0, 0, 0);

        button = new DBNButton(Icons.DATA_EDITOR_BROWSE);
        button.addMouseListener(mouseListener);
        Shortcut[] shortcuts = Keyboard.getShortcuts(IdeActions.ACTION_SHOW_INTENTION_ACTIONS);
        String shortcutText = KeymapUtil.getShortcutsText(shortcuts);

        button.setToolTipText("Open editor (" + shortcutText + ')');
        add(button, BorderLayout.EAST);

        JTextField textField = getTextField();
        if (Strings.isNotEmpty(displayValue)) {
            textField.setText(displayValue);
            textField.setEnabled(false);
            textField.setDisabledTextColor(UIUtil.getLabelDisabledForeground());
        }
        //textField.setPreferredSize(new Dimension(150, -1));
        textField.addKeyListener(keyListener);
        textField.setEditable(false);

        button.addKeyListener(keyListener);
        addKeyListener(keyListener);

        customizeButton(button);
        customizeTextField(textField);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    public JLabel getButton() {
        return button;
    }

    public void openEditor() {
        TextEditorDialog.show(getProject(), this);
    }

    /********************************************************
     *                      KeyListener                     *
     ********************************************************/
    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            Shortcut[] shortcuts = Keyboard.getShortcuts(IdeActions.ACTION_SHOW_INTENTION_ACTIONS);
            if (!keyEvent.isConsumed() && Keyboard.match(shortcuts, keyEvent)) {
                keyEvent.consume();
                openEditor();
            }
        }
    };
    /********************************************************
     *                    ActionListener                    *
     ********************************************************/
    private final ActionListener actionListener = e -> openEditor();

    private final MouseListener mouseListener = Mouse.listener().onClick(e -> openEditor());

    /********************************************************
     *                 TextEditorListener                   *
     ********************************************************/
    @Override
    public void afterUpdate() {
        Object userValue = getUserValueHolder().getUserValue();
        if (userValue instanceof String && Strings.isEmpty(displayValue)) {
            Dispatch.run(() -> {
                String text = (String) userValue;
                setEditable(text.length() < 1000 && text.indexOf('\n') == -1);
                setText(text);
            });
        }
    }

}
