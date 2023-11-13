package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBClusterMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBClusterMetadataImpl extends DBObjectMetadataBase implements DBClusterMetadata {

    public DBClusterMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getClusterName() throws SQLException {
        return getString("CLUSTER_NAME");
    }
}
