package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBTableMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTableMetadataImpl extends DBObjectMetadataBase implements DBTableMetadata {

    public DBTableMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getTableName() throws SQLException {
        return getString("TABLE_NAME");
    }

    public String getTableComment() throws SQLException {
        return getString("TABLE_COMMENT");
    }

    public boolean isTemporary() throws SQLException {
        return isYesFlag("IS_TEMPORARY");
    }

}
