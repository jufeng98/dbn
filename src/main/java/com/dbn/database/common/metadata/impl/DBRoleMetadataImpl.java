package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBRoleMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBRoleMetadataImpl extends DBObjectMetadataBase implements DBRoleMetadata {
    public DBRoleMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getRoleName() throws SQLException {
        return getString("ROLE_NAME");
    }
}
