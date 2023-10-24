package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadata;
import com.dci.intellij.dbn.object.DBConsole;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectImpl;
import com.dci.intellij.dbn.object.common.DBObjectPsiCache;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.dci.intellij.dbn.vfs.DBConsoleType;
import com.dci.intellij.dbn.vfs.file.DBConsoleVirtualFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;

import static com.dci.intellij.dbn.common.dispose.Failsafe.nd;

@Getter
public class DBConsoleImpl extends DBObjectImpl<DBObjectMetadata> implements DBConsole {
    private final DBConsoleVirtualFile virtualFile;
    private final DBConsoleType consoleType;

    public DBConsoleImpl(@NotNull ConnectionHandler connection, String name, DBConsoleType consoleType) {
        super(connection, DBObjectType.CONSOLE, name);
        virtualFile = new DBConsoleVirtualFile(this);
        this.consoleType = consoleType;
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.CONSOLE;
    }

    @NotNull
    public DBConsoleVirtualFile getVirtualFile() {
        return nd(virtualFile);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBObjectMetadata metadata) throws SQLException {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        switch (consoleType) {
            case STANDARD: return Icons.DBO_CONSOLE;
            case DEBUG: return Icons.DBO_CONSOLE_DEBUG;
        }
        return super.getIcon();
    }

    @NotNull
    @Override
    public DBObjectPsiCache getPsiCache() {
        return super.getPsiCache();
    }

    @Override
    public void setName(String newName) {
        ref().setObjectName(newName);
        virtualFile.setName(newName);
    }
}
