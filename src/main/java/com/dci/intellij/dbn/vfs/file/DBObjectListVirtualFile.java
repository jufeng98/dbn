package com.dci.intellij.dbn.vfs.file;

import com.dci.intellij.dbn.common.DevNullStreams;
import com.dci.intellij.dbn.common.dispose.Checks;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.common.util.Naming;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.DatabaseEntity;
import com.dci.intellij.dbn.connection.SchemaId;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.dci.intellij.dbn.object.common.DBObjectPsiCache;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.vfs.DBVirtualFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class DBObjectListVirtualFile<T extends DBObjectList> extends DBVirtualFileBase {
    private static final byte[] EMPTY_BYTE_CONTENT = new byte[0];
    private final WeakRef<T> objectList;

    public DBObjectListVirtualFile(T objectList) {
        super(objectList.getProject(), Naming.capitalize(objectList.getName()));
        this.objectList = WeakRef.of(objectList);
    }

    @NotNull
    public T getObjectList() {
        return objectList.ensure();
    }

    @NotNull
    @Override
    public ConnectionId getConnectionId() {
        return getObjectList().getConnectionId();
    }

    @Override
    @NotNull
    public ConnectionHandler getConnection() {
        return getObjectList().getConnection();
    }

    @Nullable
    @Override
    public SchemaId getSchemaId() {
        DatabaseEntity parent = getObjectList().getParentEntity();
        if (parent instanceof DBObject) {
            DBObject object = (DBObject) parent;
            return SchemaId.from(object.getSchema());
        }
        return null;
    }

    @Nullable
    @Override
    public DatabaseSession getSession() {
        return this.getConnection().getSessionBundle().getPoolSession();
    }

    /*********************************************************
     *                     VirtualFile                       *
     *********************************************************/

    @Override
    @NotNull
    public FileType getFileType() {
        return UnknownFileType.INSTANCE;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    @Nullable
    public VirtualFile getParent() {
        if (Checks.isValid(objectList.get())) {
            DatabaseEntity parent = getObjectList().getParentEntity();
            if (parent instanceof DBObject) {
                DBObject parentObject = (DBObject) parent;
                return DBObjectPsiCache.asPsiDirectory(parentObject).getVirtualFile();
            }

            if (parent instanceof DBObjectBundle) {
                DBObjectBundle objectBundle = (DBObjectBundle) parent;
                return objectBundle.getConnection().getPsiDirectory().getVirtualFile();
            }
        }

        return null;
    }

    @Override
    public boolean isValid() {
        return Checks.isValid(objectList.get());
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    @NotNull
    public OutputStream getOutputStream(Object o, long l, long l1) throws IOException {
        return DevNullStreams.OUTPUT_STREAM;
    }

    @Override
    @NotNull
    public byte[] contentsToByteArray() throws IOException {
        return EMPTY_BYTE_CONTENT;
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

