package com.dbn.editor.ddl;

import com.dbn.editor.EditorProviderId;
import org.jetbrains.annotations.NotNull;

public class DDLFileEditorProvider1 extends DDLFileEditorProvider {
    private DDLFileEditorProvider1() {
        super(1, "DBNavigator.DBDDLFileEditorProvider1");
    }

    @NotNull
    @Override
    public EditorProviderId getEditorProviderId() {
        return EditorProviderId.DDL1;
    }
}
