package com.dbn.execution.statement;

import com.dbn.object.DBSchema;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.type.DBObjectType;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface DataDefinitionChangeListener extends EventListener {
    Topic<DataDefinitionChangeListener> TOPIC = Topic.create("Data Model event", DataDefinitionChangeListener.class);
    void dataDefinitionChanged(@NotNull DBSchemaObject schemaObject);
    void dataDefinitionChanged(DBSchema schema, DBObjectType objectType);
}
