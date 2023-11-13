package com.dbn.editor.code.ui;

import com.dbn.common.message.MessageType;
import com.dbn.object.common.DBSchemaObject;

public class SourceCodeLoadErrorNotificationPanel extends SourceCodeEditorNotificationPanel{
    public SourceCodeLoadErrorNotificationPanel(final DBSchemaObject editableObject, String sourceLoadError) {
        super(MessageType.ERROR);
        setText("Could not load source for " + editableObject.getQualifiedNameWithType() + ". Error details: " + sourceLoadError.replace("\n", " "));
    }
}
