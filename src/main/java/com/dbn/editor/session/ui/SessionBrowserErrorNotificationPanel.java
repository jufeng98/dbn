package com.dbn.editor.session.ui;

import com.dbn.common.editor.EditorNotificationPanel;
import com.dbn.common.message.MessageType;
import com.dbn.connection.ConnectionHandler;

import javax.swing.JLabel;

public class SessionBrowserErrorNotificationPanel extends EditorNotificationPanel{
    protected final JLabel label = new JLabel();

    public SessionBrowserErrorNotificationPanel(ConnectionHandler connection, String sourceLoadError) {
        super(MessageType.ERROR);
        setText("Could not load sessions for " + connection.getName() + ". Error details: " + sourceLoadError.replace("\n", " "));
    }
}
