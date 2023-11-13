package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBUserMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUserMetadataImpl extends DBObjectMetadataBase implements DBUserMetadata {

    public DBUserMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getUserName() throws SQLException {
        return getString("USER_NAME");
    }

    public boolean isExpired() throws SQLException {
        return isYesFlag("IS_EXPIRED");
    }

    public boolean isLocked() throws SQLException {
        return isYesFlag("IS_LOCKED");
    }
}
