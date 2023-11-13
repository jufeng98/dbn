package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.action.Lookups;
import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.console.DatabaseConsoleManager;
import com.dci.intellij.dbn.debugger.DatabaseDebuggerManager;
import com.dci.intellij.dbn.vfs.file.DBConsoleVirtualFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class ConsoleSaveToFileAction extends ProjectAction {
    ConsoleSaveToFileAction() {
        super("Save to File", "Save console to file", Icons.CODE_EDITOR_SAVE_TO_FILE);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        VirtualFile file = Lookups.getVirtualFile(e);
        if (file instanceof DBConsoleVirtualFile) {
            DBConsoleVirtualFile consoleFile = (DBConsoleVirtualFile) file;
            DatabaseConsoleManager consoleManager = DatabaseConsoleManager.getInstance(project);
            consoleManager.saveConsoleToFile(consoleFile);
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        boolean visible = virtualFile instanceof DBConsoleVirtualFile && !DatabaseDebuggerManager.isDebugConsole(virtualFile);

        Presentation presentation = e.getPresentation();
        presentation.setEnabled(true);
        presentation.setVisible(visible);
    }
}