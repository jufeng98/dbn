package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBIndexColumnMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBIndexColumnMetadataImpl extends DBObjectMetadataBase implements DBIndexColumnMetadata {

    public DBIndexColumnMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getIndexName() throws SQLException {
        return getString("INDEX_NAME");
    }

    @Override
    public String getColumnName() throws SQLException {
        return getString("COLUMN_NAME");
    }

    public String getTableName() throws SQLException {
        return getString("TABLE_NAME");
    }
}
