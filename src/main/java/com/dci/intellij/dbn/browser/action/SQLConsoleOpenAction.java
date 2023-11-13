package com.dci.intellij.dbn.browser.action;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.common.action.GroupPopupAction;
import com.dci.intellij.dbn.common.action.Lookups;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.action.AbstractConnectionAction;
import com.dci.intellij.dbn.connection.console.DatabaseConsoleManager;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.object.DBConsole;
import com.dci.intellij.dbn.vfs.DBConsoleType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLConsoleOpenAction extends GroupPopupAction {
    public SQLConsoleOpenAction() {
        super("Open SQL Console", "SQL Console", Icons.FILE_SQL_CONSOLE);
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
        presentation.setEnabled(connection != null);
        presentation.setText("Open SQL Console");
    }

    @Override
    protected AnAction[] getActions(AnActionEvent e) {
        ConnectionHandler connection = getConnection(e);
        List<AnAction> actions = new ArrayList<>();
        if (connection != null) {
            Collection<DBConsole> consoles = connection.getConsoleBundle().getConsoles();
            for (DBConsole console : consoles) {
                actions.add(new SelectConsoleAction(console));
            }
            actions.add(Separator.getInstance());
            actions.add(new SelectConsoleAction(connection, DBConsoleType.STANDARD));
            if (DatabaseFeature.DEBUGGING.isSupported(connection)) {
                actions.add(new SelectConsoleAction(connection, DBConsoleType.DEBUG));
            }
        }
        return actions.toArray(new AnAction[0]);
    }


    private static class SelectConsoleAction extends AbstractConnectionAction{
        private DBConsole console;
        private DBConsoleType consoleType;

        SelectConsoleAction(@NotNull ConnectionHandler connection, @NotNull DBConsoleType consoleType) {
            super("New " + consoleType.getName() + "...", connection);
            this.consoleType = consoleType;
        }

        SelectConsoleAction(DBConsole console) {
            super(Actions.adjustActionName(console.getName()), null, console.getIcon(), console.getConnection());
            this.console = console;
        }

        @Override
        protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ConnectionHandler connection) {
            DBConsole console = this.console;
            if (console == null) {
                DatabaseConsoleManager databaseConsoleManager = DatabaseConsoleManager.getInstance(project);
                databaseConsoleManager.showCreateConsoleDialog(connection, consoleType);
            } else {
                Editors.openFileEditor(project, console.getVirtualFile(), true);
            }
        }
    }
}
