package com.dbn.connection.config.ui;

import com.dbn.connection.ConnectionId;
import com.dbn.connection.DatabaseType;
import com.intellij.util.messages.Topic;

import javax.swing.*;
import java.awt.*;
import java.util.EventListener;

public interface ConnectionPresentationChangeListener extends EventListener {
    Topic<ConnectionPresentationChangeListener> TOPIC = Topic.create("Connection presentation changed", ConnectionPresentationChangeListener.class);
    void presentationChanged(String name, Icon icon, Color color, ConnectionId connectionId, DatabaseType databaseType);
}
