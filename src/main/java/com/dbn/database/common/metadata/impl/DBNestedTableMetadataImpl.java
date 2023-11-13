package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBNestedTableMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBNestedTableMetadataImpl extends DBObjectMetadataBase implements DBNestedTableMetadata {

    public DBNestedTableMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getNestedTableName() throws SQLException {
        return getString("NESTED_TABLE_NAME");
    }

    @Override
    public String getTableName() throws SQLException {
        return getString("TABLE_NAME");
    }

    @Override
    public String getDeclaredTypeName() throws SQLException {
        return getString("DECL_TYPE_NAME");
    }

    @Override
    public String getDeclaredTypeOwner() throws SQLException {
        return getString("DECL_TYPE_OWNER");
    }
}
