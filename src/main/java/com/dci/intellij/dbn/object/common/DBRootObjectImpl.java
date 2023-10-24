package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadata;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.type.DBObjectType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

@Getter
public abstract class DBRootObjectImpl<M extends DBObjectMetadata> extends DBObjectImpl<M> implements DBRootObject {

    private volatile DBObjectListContainer childObjects;

    protected DBRootObjectImpl(@NotNull ConnectionHandler connection, M metadata) throws SQLException {
        super(connection, metadata);
    }

    protected DBRootObjectImpl(@Nullable ConnectionHandler connection, DBObjectType objectType, String name) {
        super(connection, objectType, name);
    }

    protected DBRootObjectImpl(@NotNull DBObject parentObject, M metadata) throws SQLException {
        super(parentObject, metadata);
    }

    @Override
    protected void init(ConnectionHandler connection, DBObject parentObject, M metadata) throws SQLException {
        super.init(connection, parentObject, metadata);
        initLists(connection);
    }

    @NotNull
    protected DBObjectListContainer ensureChildObjects() {
        if (childObjects == null) {
            synchronized (this) {
                if (childObjects == null) {
                    childObjects = new DBObjectListContainer(this);
                }
            }
        }
        return childObjects;
    }

    @Override
    public void disposeInner() {
        super.disposeInner();
        Disposer.dispose(childObjects);
    }
}
