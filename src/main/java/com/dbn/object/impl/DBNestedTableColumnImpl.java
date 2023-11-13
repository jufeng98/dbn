package com.dbn.object.impl;

import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.DBObjectMetadata;
import com.dbn.object.DBNestedTable;
import com.dbn.object.DBNestedTableColumn;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBObjectImpl;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

class DBNestedTableColumnImpl extends DBObjectImpl<DBObjectMetadata> implements DBNestedTableColumn {

    public DBNestedTableColumnImpl(DBNestedTable parent, DBObjectMetadata metadata) throws SQLException {
        super(parent, metadata);
        // todo !!!
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBObjectMetadata metadata) throws SQLException {
        return null; //TODO
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.NESTED_TABLE_COLUMN;
    }

    @Override
    public DBNestedTable getNestedTable() {
        return (DBNestedTable) getParentObject();
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/

    @Override
    public boolean isLeaf() {
        return true;
    }
}
