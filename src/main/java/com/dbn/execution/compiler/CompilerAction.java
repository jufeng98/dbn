package com.dbn.execution.compiler;

import com.dbn.common.ref.WeakRef;
import com.dbn.common.util.Editors;
import com.dbn.editor.DBContentType;
import com.dbn.editor.EditorProviderId;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class CompilerAction {
    private final CompilerActionSource source;
    private final DBContentType contentType;
    private WeakRef<VirtualFile> virtualFile;
    private WeakRef<FileEditor> fileEditor;
    private EditorProviderId editorProviderId;
    private int sourceStartOffset;

    public CompilerAction(CompilerActionSource source, DBContentType contentType) {
        this.source = source;
        this.contentType = contentType;
    }

    public CompilerAction(CompilerActionSource source, DBContentType contentType, @Nullable VirtualFile virtualFile, @Nullable FileEditor fileEditor) {
        this.source = source;
        this.contentType = contentType;
        this.editorProviderId = contentType.getEditorProviderId();
        setVirtualFile(virtualFile);
        setFileEditor(fileEditor);
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = WeakRef.of(virtualFile);
    }

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = WeakRef.of(fileEditor);
    }

    public boolean isDDL() {
        return source == CompilerActionSource.DDL;
    }

    public boolean isSave() {
        return source == CompilerActionSource.SAVE;
    }

    public boolean isCompile() {
        return source == CompilerActionSource.COMPILE;
    }

    public boolean isBulkCompile() {
        return source == CompilerActionSource.BULK_COMPILE;
    }

    @Nullable
    public VirtualFile getVirtualFile() {
        return WeakRef.get(virtualFile);
    }

    @Nullable
    public FileEditor getFileEditor() {
        FileEditor fileEditor = WeakRef.get(this.fileEditor);
        if (fileEditor != null) {
            Editor editor = Editors.getEditor(fileEditor);
            if (editor != null) {
                // TODO why?
                this.fileEditor = null;
            }
        }
        return fileEditor;
    }
}
