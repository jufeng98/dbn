package com.dbn.data.find.action;

import com.dbn.data.find.DataSearchComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class NextOccurrenceAction extends DataSearchHeaderAction implements DumbAware {

    public NextOccurrenceAction(DataSearchComponent searchComponent, JComponent component, boolean isSearchComponent) {
        super(searchComponent);

        ActionManager actionManager = ActionManager.getInstance();
        copyFrom(actionManager.getAction(IdeActions.ACTION_NEXT_OCCURENCE));
        Set<Shortcut> shortcuts = new HashSet<>();
        ContainerUtil.addAll(shortcuts, actionManager.getAction(IdeActions.ACTION_FIND_NEXT).getShortcutSet().getShortcuts());

        if (isSearchComponent) {
            ContainerUtil.addAll(shortcuts, actionManager.getAction(IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN).getShortcutSet().getShortcuts());
            shortcuts.add(new KeyboardShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), null));
        }
        registerShortcutsToComponent(shortcuts, this, component);
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        getSearchComponent().searchForward();
    }

    @Override
    public void update(final AnActionEvent e) {
        e.getPresentation().setEnabled(getSearchComponent().hasMatches());
    }
}
