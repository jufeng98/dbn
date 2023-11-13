package com.dbn.common.file.ui;

import com.dbn.common.file.VirtualFileInfo;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FileListCellRenderer extends ColoredListCellRenderer<VirtualFileInfo> {
    @Override
    protected void customizeCellRenderer(@NotNull JList list, VirtualFileInfo value, int index, boolean selected, boolean hasFocus) {
        Module module = value.getModule();
        if (module == null) {
            append(value.getPath(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        } else {
            VirtualFile contentRoot = value.getModuleRoot();
            VirtualFile parent = contentRoot == null ? null : contentRoot.getParent();
            int relativePathIndex = parent == null ? 0 : parent.getPath().length();
            String relativePath = value.getPath().substring(relativePathIndex);
            append('[' + module.getName() + ']', SimpleTextAttributes.REGULAR_ATTRIBUTES);
            append(relativePath, SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }

        setIcon(value.getIcon());
    }
}
