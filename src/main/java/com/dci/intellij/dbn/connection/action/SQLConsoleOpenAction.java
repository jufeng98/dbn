package com.dci.intellij.dbn.connection.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.object.DBConsole;
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
