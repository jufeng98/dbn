package com.dbn.connection.context;

import com.dbn.connection.*;
import com.dbn.connection.session.DatabaseSession;
import com.dbn.database.interfaces.DatabaseInterfaces;
import com.dbn.database.interfaces.DatabaseInterfacesProvider;
import com.dbn.object.DBSchema;
import com.dbn.object.common.DBObjectBundle;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DatabaseContext extends DatabaseInterfacesProvider {

    @Nullable
    default Project getProject() {return null;}

    @Nullable
    ConnectionId getConnectionId();

    @Nullable
    SessionId getSessionId();

    @Nullable
    SchemaId getSchemaId();

    boolean isSameAs(DatabaseContext context);

    DBSchema getSchema();

    @Nullable
    String getSchemaName();

    @Nullable
    DatabaseSession getSession();

    @Nullable
    ConnectionHandler getConnection();

    @NotNull
    ConnectionHandler ensureConnection();

    @NotNull
    DBObjectBundle getObjectBundle();

    @NotNull
    DatabaseInterfaces getInterfaces();

    ConnectionContext createConnectionContext();
}
