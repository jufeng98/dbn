package com.dbn.data.find.action;

import com.dbn.common.ui.util.Keyboard;
import com.dbn.common.util.Context;
import com.dbn.data.find.DataSearchComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class CloseOnESCAction extends DataSearchHeaderAction implements DumbAware {
    public CloseOnESCAction(final DataSearchComponent searchComponent, JComponent component) {
        super(searchComponent);

        ArrayList<Shortcut> shortcuts = new ArrayList<>();
        if (Keyboard.isEmacsKeymap()) {
            shortcuts.add(new KeyboardShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK), null));
            ActionListener actionListener = e -> {
                DataContext dataContext = Context.getDataContext(searchComponent);
                ActionManager actionManager = ActionManager.getInstance();
                AnActionEvent actionEvent = new AnActionEvent(null, dataContext, "", getTemplatePresentation(), actionManager, 2);
                CloseOnESCAction.this.actionPerformed(actionEvent);
            };
            component.registerKeyboardAction(
                    actionListener,
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                    JComponent.WHEN_FOCUSED);
        } else {
            shortcuts.add(new KeyboardShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), null));
        }

        registerCustomShortcutSet(new CustomShortcutSet(shortcuts.toArray(new Shortcut[0])), component);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        getSearchComponent().close();
    }
}
