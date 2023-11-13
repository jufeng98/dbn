package com.dbn.editor.ddl;

import com.dbn.common.editor.BasicTextEditorImpl;
import com.dbn.editor.EditorProviderId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class DDLFileEditor extends BasicTextEditorImpl<VirtualFile> {
    DDLFileEditor(Project project, VirtualFile virtualFile, EditorProviderId editorProviderId) {
        super(project, virtualFile, virtualFile.getName(), editorProviderId);
    }

}