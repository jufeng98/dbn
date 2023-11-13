package com.dbn.editor.code;

import com.dbn.editor.EditorProviderId;
import com.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;

/**
 * Only main editor extends TextEditor to force navigation of breakpoints to the right editor (e.g. package body)
 */
public class SourceCodeMainEditor extends SourceCodeEditor implements TextEditor {
    SourceCodeMainEditor(Project project, DBSourceCodeVirtualFile sourceCodeFile, String name, EditorProviderId editorProviderId) {
        super(project, sourceCodeFile, name, editorProviderId);
    }
}
