package com.dbn.editor.ddl;

import com.dbn.editor.EditorProviderId;
import org.jetbrains.annotations.NotNull;

public class DDLFileEditorProvider2 extends DDLFileEditorProvider {
    private DDLFileEditorProvider2() {
        super(2, "DBNavigator.DBDDLFileEditorProvider2");
    }

    @NotNull
    @Override
    public EditorProviderId getEditorProviderId() {
        return EditorProviderId.DDL2;
    }
}