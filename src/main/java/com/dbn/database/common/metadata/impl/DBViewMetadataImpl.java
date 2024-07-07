package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBViewMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBViewMetadataImpl extends DBObjectMetadataBase implements DBViewMetadata {

    public DBViewMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getViewName() throws SQLException {
        return getString("VIEW_NAME");
    }

    @Override
    public String getViewComment() throws SQLException {
        return getString("VIEW_COMMENT");
    }

    public String getViewType() throws SQLException {
        return getString("VIEW_TYPE");
    }

    public String getViewTypeOwner() throws SQLException {
        return getString("VIEW_TYPE_OWNER");
    }

    public boolean isSystemView() throws SQLException {
        return isYesFlag("IS_SYSTEM_VIEW");
    }
}
