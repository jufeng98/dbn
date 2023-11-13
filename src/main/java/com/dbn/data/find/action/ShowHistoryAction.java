package com.dbn.data.find.action;

import com.dbn.data.find.DataSearchComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ShowHistoryAction extends DataSearchHeaderAction implements DumbAware {
    private JTextField searchField;


    public ShowHistoryAction(final JTextField searchField, DataSearchComponent searchComponent) {
        super(searchComponent);
        this.searchField = searchField;
        getTemplatePresentation().setIcon(IconLoader.getIcon("/actions/search.png"));
        getTemplatePresentation().setDescription("Search history");
        getTemplatePresentation().setText("Search History");

        ArrayList<Shortcut> shortcuts = new ArrayList<>();
        ContainerUtil.addAll(shortcuts, ActionManager.getInstance().getAction("IncrementalSearch").getShortcutSet().getShortcuts());
        shortcuts.add(new KeyboardShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), null));

        registerCustomShortcutSet(new CustomShortcutSet(shortcuts.toArray(new Shortcut[0])), searchField);
        searchField.registerKeyboardAction(actionEvent -> {
            if (searchField.getText().isEmpty()) {
                getSearchComponent().showHistory(false, searchField);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_FOCUSED);
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        getSearchComponent().showHistory(e.getInputEvent() instanceof MouseEvent, searchField);
    }


}
