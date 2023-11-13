package com.dci.intellij.dbn.menu.action;

import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.ConnectionBundle;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.action.AbstractConnectionAction;
import com.dci.intellij.dbn.editor.session.SessionBrowserManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SessionBrowserOpenAction extends ProjectAction {
    public SessionBrowserOpenAction() {
        super("Open Session Browser...", null, Icons.FILE_SESSION_BROWSER);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        //FeatureUsageTracker.getInstance().triggerFeatureUsed("navigation.popup.file");
        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        ConnectionBundle connectionBundle = connectionManager.getConnectionBundle();
        List<ConnectionHandler> connections = connectionBundle.getConnections();
        if (connections.size() == 0) {
            connectionManager.promptMissingConnection();
            return;
        }

        if (connections.size() == 1) {
            openSessionBrowser(connections.get(0));
            return;
        }

        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.addSeparator();
        for (ConnectionHandler connection : connections) {
            actionGroup.add(new SelectConnectionAction(connection));
        }

        ListPopup popupBuilder = JBPopupFactory.getInstance().createActionGroupPopup(
                "Select Session Browser Connection",
                actionGroup,
                e.getDataContext(),
                //JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                false,
                true,
                true,
                null,
                actionGroup.getChildrenCount(), null);

        popupBuilder.showCenteredInCurrentWindow(project);
    }

    private static class SelectConnectionAction extends AbstractConnectionAction{

        SelectConnectionAction(ConnectionHandler connection) {
            super(connection.getName(), connection.getIcon(), connection);
        }

        @Override
        protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ConnectionHandler connection) {
            openSessionBrowser(connection);
        }
    }

    private static void openSessionBrowser(ConnectionHandler connection) {
        SessionBrowserManager sessionBrowserManager = SessionBrowserManager.getInstance(connection.getProject());
        sessionBrowserManager.openSessionBrowser(connection);
    }
}
