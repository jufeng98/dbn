package com.dbn.vfs.file;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.vfs.DBVirtualFileBase;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DBConnectionVirtualFile extends DBVirtualFileBase {
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

    @Override
    public boolean isDirectory() {
        return true;
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
    public String getExtension() {
        return null;
    }
}

