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

public class ConsoleRenameAction extends ProjectAction {
    ConsoleRenameAction() {
        super("Rename Console", null, Icons.ACTION_EDIT);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);

        if (virtualFile instanceof DBConsoleVirtualFile) {
            DBConsoleVirtualFile consoleVirtualFile = (DBConsoleVirtualFile) virtualFile;
            DatabaseConsoleManager consoleManager = DatabaseConsoleManager.getInstance(project);
            consoleManager.showRenameConsoleDialog(consoleVirtualFile.getConsole());
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);

        Presentation presentation = e.getPresentation();
        presentation.setEnabled(virtualFile instanceof DBConsoleVirtualFile);
    }


}