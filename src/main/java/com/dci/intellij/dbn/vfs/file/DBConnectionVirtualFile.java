package com.dci.intellij.dbn.vfs.file;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.SchemaId;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.dci.intellij.dbn.vfs.DBVirtualFileBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;

public class DBConnectionVirtualFile extends DBVirtualFileBase {
    private static final byte[] EMPTY_CONTENT = new byte[0];
    private final ConnectionRef connection;

    public DBConnectionVirtualFile(ConnectionHandler connection) {
        super(connection.getProject(), connection.getName());
        this.connection = connection.ref();
    }

    @Override
    @NotNull
    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @Nullable
    @Override
    public SchemaId getSchemaId() {
        return null;
    }

    @Nullable
    @Override
    public DatabaseSession getSession() {
        return null;
    }

    /*********************************************************
     *                     VirtualFile                       *
     *********************************************************/

    @Override
    public boolean isValid() {
        return connection.isValid();
    }

    @Override
    public Icon getIcon() {
        return getConnection().getIcon();
    }

    @Override
    @NotNull
    public byte[] contentsToByteArray() throws IOException {
        return EMPTY_CONTENT;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public void refresh(boolean b, boolean b1, Runnable runnable) {

    }

    @Override
    public String getExtension() {
        return null;
    }
}

