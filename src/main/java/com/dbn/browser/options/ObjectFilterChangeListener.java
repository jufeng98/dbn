package com.dbn.browser.options;

import com.dbn.connection.ConnectionId;
import com.dbn.object.type.DBObjectType;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface ObjectFilterChangeListener extends EventListener {
    Topic<ObjectFilterChangeListener> TOPIC = Topic.create("Object filter changed", ObjectFilterChangeListener.class);
    void typeFiltersChanged(ConnectionId connectionId);
    void nameFiltersChanged(ConnectionId connectionId, @NotNull DBObjectType[] objectTypes);
}
