package com.dbn.editor.console;

import com.dbn.common.editor.BasicTextEditorImpl;
import com.dbn.common.editor.BasicTextEditorState;
import com.dbn.editor.EditorProviderId;
import com.dbn.vfs.file.DBConsoleVirtualFile;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;

public class SQLConsoleEditor extends BasicTextEditorImpl<DBConsoleVirtualFile> implements TextEditor {
    SQLConsoleEditor(Project project, DBConsoleVirtualFile sqlConsoleFile, String name, EditorProviderId editorProviderId) {
        super(project, sqlConsoleFile, name, editorProviderId);
    }

    @Override
    protected BasicTextEditorState createEditorState() {
        return new SQLConsoleEditorState();
    }

}
