package com.dbn.connection;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ConnectionLoadListener extends EventListener {
    Topic<ConnectionLoadListener> TOPIC = Topic.create("meta-data load event", ConnectionLoadListener.class);
    void contentsLoaded(ConnectionHandler connection);
}
