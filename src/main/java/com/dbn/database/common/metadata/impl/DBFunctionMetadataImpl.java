package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.def.DBFunctionMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFunctionMetadataImpl extends DBMethodMetadataImpl implements DBFunctionMetadata {
    public DBFunctionMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getFunctionName() throws SQLException {
        return getString("FUNCTION_NAME");
    }
}
