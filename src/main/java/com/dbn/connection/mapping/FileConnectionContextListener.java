package com.dbn.connection.mapping;

import com.dbn.connection.session.DatabaseSession;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SchemaId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface FileConnectionContextListener extends EventListener {
    Topic<FileConnectionContextListener> TOPIC = Topic.create("Connection mapping changed", FileConnectionContextListener.class);

    default void connectionChanged(Project project, VirtualFile file, ConnectionHandler connection){
        mappingChanged(project, file);
    }

    default void schemaChanged(Project project, VirtualFile file, SchemaId schema){
        mappingChanged(project, file);
    }

    default void sessionChanged(Project project, VirtualFile file, DatabaseSession session){
        mappingChanged(project, file);
    }

    default void mappingChanged(Project project, VirtualFile file){}
}
