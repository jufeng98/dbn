package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.action.Lookups;
import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.console.DatabaseConsoleManager;
import com.dci.intellij.dbn.vfs.file.DBConsoleVirtualFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class ConsoleDeleteAction extends ProjectAction {
    ConsoleDeleteAction() {
        super("Delete Console", null, Icons.ACTION_CLOSE);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        if (virtualFile instanceof DBConsoleVirtualFile) {
            DBConsoleVirtualFile consoleVirtualFile = (DBConsoleVirtualFile) virtualFile;
            DatabaseConsoleManager consoleManager = DatabaseConsoleManager.getInstance(project);
            consoleManager.deleteConsole(consoleVirtualFile.getConsole());
        }
    }


    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);

        Presentation presentation = e.getPresentation();
        presentation.setEnabled(virtualFile instanceof DBConsoleVirtualFile);
    }


}