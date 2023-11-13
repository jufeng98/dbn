package com.dbn.editor.code.ui;

import com.dbn.common.editor.EditorNotificationPanel;
import com.dbn.common.message.MessageType;

public abstract class SourceCodeEditorNotificationPanel extends EditorNotificationPanel {
    public SourceCodeEditorNotificationPanel(MessageType messageType) {
        super(messageType);
    }
}
