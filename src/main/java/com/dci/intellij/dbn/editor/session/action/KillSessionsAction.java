package com.dci.intellij.dbn.editor.session.action;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.editor.session.SessionBrowser;
import com.dci.intellij.dbn.editor.session.ui.table.SessionBrowserTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;

public class KillSessionsAction extends AbstractSessionBrowserAction {

    public KillSessionsAction() {
        super("Kill Sessions", Icons.ACTION_KILL_SESSION);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        if (sessionBrowser != null) {
            sessionBrowser.killSelectedSessions();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        boolean visible = false;
        boolean enabled = false;
        if (sessionBrowser != null) {
            ConnectionHandler connection = Failsafe.nn(sessionBrowser.getConnection());
            visible = DatabaseFeature.SESSION_KILL.isSupported(connection);
            SessionBrowserTable editorTable = sessionBrowser.getBrowserTable();
            enabled = editorTable.getSelectedRows().length > 0;
        }

        Presentation presentation = e.getPresentation();
        presentation.setText("Kill Sessions");
        presentation.setVisible(visible);
        presentation.setEnabled(enabled);
    }
}