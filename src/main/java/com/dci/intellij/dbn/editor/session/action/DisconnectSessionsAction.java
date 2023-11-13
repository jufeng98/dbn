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

public class DisconnectSessionsAction extends AbstractSessionBrowserAction {

    public DisconnectSessionsAction() {
        super("Disconnect Sessions", Icons.ACTION_DISCONNECT_SESSION);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        if (sessionBrowser != null) {
            sessionBrowser.disconnectSelectedSessions();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        boolean visible = false;
        boolean enabled = false;
        if (sessionBrowser != null) {
            ConnectionHandler connection = Failsafe.nn(sessionBrowser.getConnection());
            visible = DatabaseFeature.SESSION_DISCONNECT.isSupported(connection);
            SessionBrowserTable editorTable = sessionBrowser.getBrowserTable();
            enabled = editorTable.getSelectedRows().length > 0;
        }

        Presentation presentation = e.getPresentation();
        presentation.setText("Disconnect Sessions");
        presentation.setVisible(visible);
        presentation.setEnabled(enabled);
    }
}