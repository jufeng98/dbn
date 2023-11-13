package com.dbn.connection;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ConnectionHandlerStatusListener extends EventListener {
    Topic<ConnectionHandlerStatusListener> TOPIC = Topic.create("Connection handler status changed", ConnectionHandlerStatusListener.class);
    void statusChanged(ConnectionId connectionId);
}
