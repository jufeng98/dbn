package com.dbn.common.compatibility;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CompatibilityUtil {
    public static void setSmallerFont(JComponent component) {
        Utils.setSmallerFont(component);
    }

    public static boolean isUnderGTKLookAndFeel() {
        return SystemInfo.isXWindow && UIManager.getLookAndFeel().getName().contains("GTK");
    }

    @Nullable
    public static FileEditor getSelectedEditor(Project project) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        //return fileEditorManager.getSelectedEditor();

        VirtualFile[] files = fileEditorManager.getSelectedFiles();
        return files.length == 0 ? null : fileEditorManager.getSelectedEditor(files[0]);
    }
}
