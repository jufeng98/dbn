package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.def.DBMaterializedViewMetadata;

import java.sql.ResultSet;

public class DBMaterializedViewMetadataImpl extends DBViewMetadataImpl implements DBMaterializedViewMetadata {

    public DBMaterializedViewMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }
}
