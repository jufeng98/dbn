package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadata;
import com.dci.intellij.dbn.object.DBNestedTable;
import com.dci.intellij.dbn.object.DBNestedTableColumn;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectImpl;
import com.dci.intellij.dbn.object.type.DBObjectType;
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
