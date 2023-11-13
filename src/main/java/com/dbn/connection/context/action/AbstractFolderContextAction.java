package com.dbn.connection.context.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.connection.mapping.FileConnectionContext;
import com.dbn.connection.mapping.FileConnectionContextManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFolderContextAction extends ProjectAction {

    protected static FileConnectionContext getFileContext(@Nullable VirtualFile file, @NotNull Project project) {
        if (file == null || !file.isDirectory()) return null;

        FileConnectionContextManager contextManager = getContextManager(project);
        FileConnectionContext mapping = contextManager.getMapping(file);
        if (mapping != null && mapping.isForFile(file)) {
            return mapping;
        }
        return null;
    }

    protected static FileConnectionContextManager getContextManager(@NotNull Project project) {
        return FileConnectionContextManager.getInstance(project);
    }
}
