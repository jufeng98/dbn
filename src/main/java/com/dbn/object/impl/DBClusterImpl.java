package com.dbn.object.impl;

import com.dbn.browser.ui.HtmlToolTipBuilder;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBClusterMetadata;
import com.dbn.object.DBCluster;
import com.dbn.object.DBSchema;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObjectImpl;
import com.dbn.object.type.DBObjectType;
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
