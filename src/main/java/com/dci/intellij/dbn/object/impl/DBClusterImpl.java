package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBClusterMetadata;
import com.dci.intellij.dbn.object.DBCluster;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

class DBClusterImpl extends DBSchemaObjectImpl<DBClusterMetadata> implements DBCluster {
    DBClusterImpl(DBSchema parent, DBClusterMetadata resultSet) throws SQLException {
        super(parent, resultSet);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBClusterMetadata metadata) throws SQLException {
        return metadata.getClusterName();
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.CLUSTER;
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/

    @Override
    public boolean isLeaf() {
        return true;
    }

}
