package com.dbn.vfs;

import com.intellij.ide.FileIconProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DBFileIconProvider implements FileIconProvider{
    @Override
    public Icon getIcon(@NotNull VirtualFile file, int flags, @Nullable Project project) {
        if (file instanceof DBVirtualFileBase) {
            DBVirtualFileBase virtualFile = (DBVirtualFileBase) file;
            return virtualFile.getIcon();
        }
        return null;
    }
}
