package com.dci.intellij.dbn.object.action;

import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.console.DatabaseConsoleManager;
import com.dci.intellij.dbn.object.DBConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ConsoleDeleteAction extends ProjectAction {
    private DBConsole console;

    ConsoleDeleteAction(DBConsole console) {
        super("Delete Console", null, Icons.ACTION_CLOSE);
        this.console = console;
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DatabaseConsoleManager consoleManager = DatabaseConsoleManager.getInstance(project);
        consoleManager.deleteConsole(console);
    }
}