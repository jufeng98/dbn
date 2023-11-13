package com.dbn.common.editor;

import com.dbn.common.file.VirtualFileDelegate;
import com.dbn.editor.EditorProviderId;
import com.dbn.language.common.DBLanguageFileType;
import com.dbn.language.sql.SQLFileType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.dbn.editor.EditorProviderId.DBN_SQL;
import static com.intellij.openapi.fileEditor.FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;

public class SQLFileTextEditorProvider extends BasicTextEditorProvider{
    @NotNull
    @Override
    public EditorProviderId getEditorProviderId() {
        return DBN_SQL;
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        String extension = virtualFile.getExtension();

        FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        FileType fileType = fileTypeManager.getFileTypeByFile(virtualFile);
        if (fileType instanceof DBLanguageFileType) return false;

        if (SQLFileType.INSTANCE.isSupported(virtualFile.getExtension())) {
            return true;
        }
        return false;
    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        VirtualFile virtualFileDelegate = new VirtualFileDelegate(virtualFile, SQLFileType.INSTANCE);
        return new BasicTextEditorImpl(project, virtualFileDelegate, "DBN SQL", getEditorProviderId()){};
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy() {
        return PLACE_AFTER_DEFAULT_EDITOR;
    }


    @NotNull
    @Override
    public String getComponentName() {
        return "DBNavigator.SQLFileTextEditorProvider";
    }
}
