package com.dbn.connection.mapping;

import com.dbn.common.state.PersistentStateElement;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.SchemaId;
import com.dbn.connection.SessionId;
import com.dbn.connection.context.DatabaseContextBase;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileConnectionContext extends DatabaseContextBase, PersistentStateElement {
    String getFileUrl();

    @Nullable
    VirtualFile getFile();

    default boolean isForFile(@NotNull VirtualFile file) {
        return file.equals(getFile());
    }

    default void setFileUrl(String fileUrl) {
        throw new UnsupportedOperationException();
    }

    default boolean setConnectionId(@Nullable ConnectionId connectionId) {
        throw new UnsupportedOperationException();
    }

    default boolean setSessionId(@Nullable SessionId sessionId) {
        throw new UnsupportedOperationException();
    }

    default boolean setSchemaId(@Nullable SchemaId schemaId) {
        throw new UnsupportedOperationException();
    }

}
