package com.dbn.execution.compiler;

import com.dbn.connection.ConnectionHandler;
import com.dbn.object.common.DBSchemaObject;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

public interface CompileManagerListener extends EventListener {
    Topic<CompileManagerListener> TOPIC = Topic.create("Compile Manager Event", CompileManagerListener.class);

    void compileFinished(@NotNull ConnectionHandler connection, @Nullable DBSchemaObject object);
}
