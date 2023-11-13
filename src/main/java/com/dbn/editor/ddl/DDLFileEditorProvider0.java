package com.dbn.editor.ddl;

import com.dbn.editor.EditorProviderId;
import org.jetbrains.annotations.NotNull;

public class DDLFileEditorProvider0 extends DDLFileEditorProvider {
    private DDLFileEditorProvider0() {
        super(0, "DBNavigator.DBDDLFileEditorProvider0");
    }

    @NotNull
    @Override
    public EditorProviderId getEditorProviderId() {
        return EditorProviderId.DDL0;
    }
}