package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBIndexMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBIndexMetadataImpl extends DBObjectMetadataBase implements DBIndexMetadata {

    public DBIndexMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getIndexName() throws SQLException {
        return getString("INDEX_NAME");
    }

    public String getTableName() throws SQLException {
        return getString("TABLE_NAME");
    }

    public boolean isUnique() throws SQLException {
        return isYesFlag("IS_UNIQUE");
    }

    public boolean isValid() throws SQLException {
        return isYesFlag("IS_VALID");
    }

}
