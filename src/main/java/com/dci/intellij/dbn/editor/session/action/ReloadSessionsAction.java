package com.dci.intellij.dbn.editor.session.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.session.SessionBrowser;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;

public class ReloadSessionsAction extends AbstractSessionBrowserAction {

    public ReloadSessionsAction() {
        super("Reload", Icons.DATA_EDITOR_RELOAD_DATA);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        if (sessionBrowser != null) {
            sessionBrowser.loadSessions(true);
        }
    }

    @Override
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        presentation.setEnabled(sessionBrowser != null && !sessionBrowser.isLoading());
        presentation.setText("Reload");
        presentation.setEnabled(true);

    }
}