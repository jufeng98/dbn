package com.dbn.connection.action;

import com.dbn.common.icon.Icons;
import com.dbn.common.util.Editors;
import com.dbn.connection.ConnectionHandler;
import com.dbn.object.DBConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SQLConsoleOpenAction extends AbstractConnectionAction {
    SQLConsoleOpenAction(ConnectionHandler connection) {
        super("Open SQL Console", Icons.FILE_SQL_CONSOLE, connection);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ConnectionHandler connection) {
        DBConsole defaultConsole = connection.getConsoleBundle().getDefaultConsole();
        Editors.openFileEditor(project, defaultConsole.getVirtualFile(), true);
    }
}
