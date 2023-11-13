package com.dbn.execution.method.action;

import com.dbn.connection.SchemaId;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.object.DBSchema;
import com.dbn.object.action.AnObjectAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ExecutionSchemaSelectAction extends AnObjectAction<DBSchema> {
    private MethodExecutionInput executionInput;

    ExecutionSchemaSelectAction(MethodExecutionInput executionInput, DBSchema schema) {
        super(schema);
        this.executionInput = executionInput;
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull DBSchema object) {

        executionInput.setTargetSchemaId(SchemaId.from(object));
    }
}
