package com.dbn.connection;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ConnectionStatusListener extends EventListener {
    Topic<ConnectionStatusListener> TOPIC = Topic.create("Connection status changed", ConnectionStatusListener.class);
    void statusChanged(ConnectionId connectionId, SessionId sessionId);
}
