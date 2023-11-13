package com.dci.intellij.dbn.object.action;

import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.console.DatabaseConsoleManager;
import com.dci.intellij.dbn.object.DBConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ConsoleRenameAction extends ProjectAction {
    private DBConsole console;

    public ConsoleRenameAction(DBConsole console) {
        super("Rename Console", null, Icons.ACTION_EDIT);
        this.console = console;
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DatabaseConsoleManager consoleManager = DatabaseConsoleManager.getInstance(project);
        consoleManager.showRenameConsoleDialog(console);
    }
}