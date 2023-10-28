package com.dci.intellij.dbn.vfs.file;

import com.dci.intellij.dbn.common.property.PropertyHolder;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.SchemaId;
import com.dci.intellij.dbn.ddl.DDLFileManager;
import com.dci.intellij.dbn.ddl.DDLFileType;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.psql.PSQLLanguage;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBView;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.dci.intellij.dbn.vfs.DBVirtualFileBase;
import com.dci.intellij.dbn.vfs.file.status.DBFileStatus;
import com.dci.intellij.dbn.vfs.file.status.DBFileStatusHolder;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

import static com.dci.intellij.dbn.vfs.file.status.DBFileStatus.MODIFIED;

@Getter
public abstract class DBContentVirtualFile extends DBVirtualFileBase implements PropertyHolder<DBFileStatus>  {
    private final WeakRef<DBEditableObjectVirtualFile> mainDatabaseFile;
    private final FileType fileType;

    private final DBFileStatusHolder status = new DBFileStatusHolder(this);

    protected DBContentType contentType;

    public DBContentVirtualFile(@NotNull DBEditableObjectVirtualFile mainDatabaseFile, DBContentType contentType) {
        super(mainDatabaseFile.getProject(), mainDatabaseFile.getObjectRef().getObjectName());
        this.mainDatabaseFile = WeakRef.of(mainDatabaseFile);
        this.contentType = contentType;

        DBObjectRef<DBSchemaObject> objectRef = mainDatabaseFile.getObjectRef();

        Project project = getProject();
        DDLFileManager ddlFileManager = DDLFileManager.getInstance(project);
        DDLFileType ddlFileType = ddlFileManager.getDDLFileType(objectRef.getObjectType(), contentType);
        this.fileType = ddlFileType == null ? null : ddlFileType.getLanguageFileType();
    }

    @Override
    public boolean set(DBFileStatus status, boolean value) {
        return this.status.set(status, value);
    }

    @Override
    public boolean is(DBFileStatus status) {
        return this.status.is(status);
    }
    @Override
    @Nullable
    public SchemaId getSchemaId() {
        DBSchema schema = getObject().getSchema();
        return SchemaId.from(schema);
    }

    @NotNull
    public DBEditableObjectVirtualFile getMainDatabaseFile() {
        return mainDatabaseFile.ensure();
    }

    @Override
    public boolean isValid() {
        DBEditableObjectVirtualFile mainDatabaseFile = this.mainDatabaseFile.get();
        return mainDatabaseFile != null && mainDatabaseFile.isValid();
    }

    @NotNull
    public DBSchemaObject getObject() {
        return getMainDatabaseFile().getObject();
    }

    @NotNull
    public DBObjectRef<DBSchemaObject> getObjectRef() {
        return getMainDatabaseFile().getObjectRef();
    }


    @NotNull
    @Override
    public ConnectionId getConnectionId() {
        return getMainDatabaseFile().getConnectionId();
    }

    @NotNull
    @Override
    public ConnectionHandler getConnection() {
        return getMainDatabaseFile().getConnection();
    }

    public DBLanguageDialect getLanguageDialect() {
        DBSchemaObject object = getObject();
        DBLanguage language =
                object instanceof DBView ?
                        SQLLanguage.INSTANCE :
                        PSQLLanguage.INSTANCE;
        
        return object.getLanguageDialect(language);
    }

    /*********************************************************
     *                     VirtualFile                       *
     *********************************************************/
    @NotNull
    public String getPresentablePath() {
        DBObjectRef<DBSchemaObject> object = getMainDatabaseFile().getObjectRef();
        return getConnection().getName() + File.separatorChar +
                object.getObjectType().getListName() + File.separatorChar +
                object.getQualifiedName() + " - " + getContentType().getDescription();
    }

    @NotNull
    @Override
    public String getPresentableName() {
        DBObjectRef<DBSchemaObject> object = getMainDatabaseFile().getObjectRef();
        return object.getObjectName() + " - " + getContentType().getDescription();
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    @Nullable
    public VirtualFile getParent() {
        if (!isValid()) return null;

        DBObjectRef parentObject = getObjectRef().getParentRef();
        if (parentObject == null) return null;

        return DBObjectVirtualFile.of(parentObject);
    }

    @Override
    public Icon getIcon() {
        DBObjectType objectType = getObjectRef().getObjectType();
        DBContentType contentType = getContentType();
        return objectType.getIcon(contentType);
    }

    @Override
    public void refresh(boolean b, boolean b1, Runnable runnable) {

    }

    public void setModified(boolean modified) {
        set(MODIFIED, modified);
    }

    public boolean isModified() {
        return is(MODIFIED);
    }
}
