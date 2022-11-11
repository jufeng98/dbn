package com.dci.intellij.dbn.vfs.file;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.common.DevNullStreams;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.util.Traces;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.SchemaId;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.dci.intellij.dbn.vfs.DBVirtualFileImpl;
import com.intellij.ide.navigationToolbar.NavBarPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBObjectVirtualFile<T extends DBObject> extends DBVirtualFileImpl {
    private static final byte[] EMPTY_BYTE_CONTENT = new byte[0];
    protected DBObjectRef<T> object;

    public DBObjectVirtualFile(@NotNull Project project, @NotNull DBObjectRef<T> object) {
        super(project);
        this.object = object;
        this.name = object.getFileName();
    }

    public DBObjectType getObjectType() {
        return object.getObjectType();
    }

    public DBObjectRef<T> getObjectRef() {
        return object;
    }

    @NotNull
    public T getObject() {
        return DBObjectRef.ensure(object);
    }

    @NotNull
    @Override
    public final ConnectionId getConnectionId() {
        return object.getConnectionId();
    }

    @Override
    @NotNull
    public ConnectionHandler getConnection() {
        return Failsafe.nn(object.getConnection());
    }

    @Nullable
    @Override
    public SchemaId getSchemaId() {
        return SchemaId.from(getObject().getSchema());
    }

    @Override
    public DatabaseSession getSession() {
        return getConnection().getSessionBundle().getPoolSession();
    }

    @Override
    public boolean isValid() {
        return super.isValid() && object.get() != null;
    }

    @NotNull
    @Override
    public String getPresentablePath() {
        return getConnection().getName() + File.separatorChar +
                getObjectRef().getObjectType().getListName() + File.separatorChar +
                getObjectRef().getQualifiedName();
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
    public boolean isWritable() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    @Nullable
    public VirtualFile getParent() {
        if (Traces.isCalledThrough(NavBarPresentation.class)) {
            T object = getObject();
            BrowserTreeNode treeParent = object.getParent();
            if (treeParent instanceof DBObjectList<?>) {
                DBObjectList objectList = (DBObjectList) treeParent;
                return objectList.getPsiDirectory().getVirtualFile();
            }
        }
        return null;
    }



    @Override
    public Icon getIcon() {
        return object.getObjectType().getIcon();
    }

    @Override
    public VirtualFile[] getChildren() {
        return VirtualFile.EMPTY_ARRAY;
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
    public long getTimeStamp() {
        return 0;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public void refresh(boolean b, boolean b1, Runnable runnable) {

    }

    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return DevNullStreams.INPUT_STREAM;
    }

    @Override
    public long getModificationStamp() {
        return 1;
    }

    @Override
    public String getExtension() {
        return null;
    }
}
