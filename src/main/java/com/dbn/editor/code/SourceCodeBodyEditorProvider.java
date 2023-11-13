package com.dbn.editor.code;

import com.dbn.editor.DBContentType;
import com.dbn.editor.EditorProviderId;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SourceCodeBodyEditorProvider extends SourceCodeEditorProviderBase {

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        if (virtualFile instanceof DBEditableObjectVirtualFile) {
            DBEditableObjectVirtualFile databaseFile = (DBEditableObjectVirtualFile) virtualFile;
            DBContentType contentType = databaseFile.getContentType();
            return contentType == DBContentType.CODE_SPEC_AND_BODY;
        }

        return super.accept(project, virtualFile);
    }

    @Override
    public DBContentType getContentType() {
        return DBContentType.CODE_BODY;
    }

    @Override
    @NotNull
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;

    }

    @NotNull
    @Override
    public EditorProviderId getEditorProviderId() {
        return EditorProviderId.CODE_BODY;
    }

    @Override
    public String getName() {
        return "Body";
    }

    @Override
    public Icon getIcon() {
        return null;//Icons.CODE_EDITOR_BODY;
    }

    /*********************************************************
     *                ApplicationComponent                   *
     *********************************************************/

    @Override
    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.DBSourceBodyEditorProvider";
    }
}
