package com.dci.intellij.dbn.browser.action;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.common.action.Lookups;
import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.editor.session.SessionBrowserManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SessionBrowserOpenAction extends ProjectAction {
    public SessionBrowserOpenAction() {
        super("Open Session Browser", "", Icons.FILE_SESSION_BROWSER);
    }

    private static ConnectionHandler getConnection(@NotNull AnActionEvent e) {
        Project project = Lookups.getProject(e);
        if (project != null) {
            DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
            return browserManager.getActiveConnection();
        }
        return null;
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        ConnectionHandler connection = getConnection(e);
        if (connection != null) {
            presentation.setEnabled(true);
            presentation.setVisible(DatabaseFeature.SESSION_BROWSING.isSupported(connection));
        } else {
            presentation.setVisible(false);
            presentation.setEnabled(false);
        }
        presentation.setText("Open Session Browser");
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ConnectionHandler connection = getConnection(e);
        if (connection != null) {
            SessionBrowserManager sessionBrowserManager = SessionBrowserManager.getInstance(project);
            sessionBrowserManager.openSessionBrowser(connection);
        }

    }

}
