package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBObjectDependencyMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBObjectDependencyMetadataImpl extends DBObjectMetadataBase implements DBObjectDependencyMetadata {

    public DBObjectDependencyMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getObjectName() throws SQLException {
        return getString("OBJECT_NAME");
    }

    @Override
    public String getObjectOwner() throws SQLException {
        return getString("OBJECT_OWNER");
    }

    @Override
    public String getObjectType() throws SQLException {
        return getString("OBJECT_TYPE");
    }
}
