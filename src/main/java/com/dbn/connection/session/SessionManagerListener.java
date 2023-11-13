package com.dbn.connection.session;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface SessionManagerListener extends EventListener{
    Topic<SessionManagerListener> TOPIC = Topic.create("Session manager event", SessionManagerListener.class);

    default void sessionCreated(DatabaseSession session) {};

    default void sessionDeleted(DatabaseSession session) {};

    default void sessionChanged(DatabaseSession session){};
}
