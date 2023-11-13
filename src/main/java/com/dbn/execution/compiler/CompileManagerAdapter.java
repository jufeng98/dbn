package com.dbn.execution.compiler;

import com.dbn.connection.ConnectionHandler;
import com.dbn.object.common.DBSchemaObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CompileManagerAdapter implements CompileManagerListener {
    @Override
    public void compileFinished(@NotNull ConnectionHandler connection, @Nullable DBSchemaObject object) {

    }
}
