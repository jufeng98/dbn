package com.dbn.database.interfaces.queue;

import com.dbn.common.Priority;
import com.dbn.connection.ConnectionContext;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.SchemaId;
import com.intellij.openapi.project.Project;
import lombok.Getter;

@Getter
public class InterfaceTaskRequest extends ConnectionContext{
    private final String title;
    private final String text;
    private final Priority priority;


    private InterfaceTaskRequest(Project project, String title, String text, Priority priority, ConnectionId connectionId, SchemaId schemaId) {
        super(project, connectionId, schemaId);
        this.title = title;
        this.text = text;
        this.priority = priority;
    }

    public static InterfaceTaskRequest create(Priority priority, String title, String text, Project project, ConnectionId connectionId, SchemaId schemaId) {
        return new InterfaceTaskRequest(project, title, text, priority, connectionId, schemaId);
    }
}
