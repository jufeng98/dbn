package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBDatabaseLinkMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDatabaseLinkMetadataImpl extends DBObjectMetadataBase implements DBDatabaseLinkMetadata {
    public DBDatabaseLinkMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getDblinkName() throws SQLException {
        return getString("DBLINK_NAME");
    }

    public String getUserName() throws SQLException {
        return getString("USER_NAME");
    }

    public String getHost() throws SQLException {
        return getString("HOST");
    }
}
