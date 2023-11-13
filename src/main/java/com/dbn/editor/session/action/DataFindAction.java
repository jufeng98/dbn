package com.dbn.editor.session.action;

import com.dbn.common.icon.Icons;
import com.dbn.editor.session.SessionBrowser;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DataFindAction extends AbstractSessionBrowserAction {
    public DataFindAction() {
        super("Find...", Icons.ACTION_FIND);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        if (sessionBrowser != null) {
            sessionBrowser.showSearchHeader();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        e.getPresentation().setEnabled(sessionBrowser != null);
        e.getPresentation().setText("Find...");
    }
}