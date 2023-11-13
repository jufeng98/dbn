package com.dbn.execution.method;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SchemaId;
import com.dbn.execution.ExecutionContext;
import com.dbn.execution.ExecutionOptions;
import com.dbn.object.lookup.DBObjectRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MethodExecutionContext extends ExecutionContext<MethodExecutionInput> {
    public MethodExecutionContext(MethodExecutionInput input) {
        super(input);
    }

    @NotNull
    @Override
    public String getTargetName() {
        DBObjectRef method = getInput().getMethodRef();
        return method.getObjectType().getName() + " " + method.getObjectName();
    }

    @Nullable
    @Override
    public ConnectionHandler getTargetConnection() {
        return getInput().getConnection();
    }

    @Nullable
    @Override
    public SchemaId getTargetSchema() {
        return getInput().getTargetSchemaId();
    }

    public ExecutionOptions getOptions() {
        return getInput().getOptions();
    }
}
