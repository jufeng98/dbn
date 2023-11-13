package com.dbn.editor.session.action;

import com.dbn.common.dispose.Failsafe;
import com.dbn.common.icon.Icons;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.DatabaseFeature;
import com.dbn.editor.session.SessionBrowser;
import com.dbn.editor.session.ui.table.SessionBrowserTable;
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