package com.dbn.object.factory;

import com.dbn.object.common.DBSchemaObject;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ObjectFactoryListener extends EventListener{
    Topic<ObjectFactoryListener> TOPIC = Topic.create("Object Factory Event", ObjectFactoryListener.class);
    void objectCreated(DBSchemaObject object);
    void objectDropped(DBSchemaObject object);
}
