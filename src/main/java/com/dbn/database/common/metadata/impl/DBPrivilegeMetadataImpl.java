package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBPrivilegeMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBPrivilegeMetadataImpl extends DBObjectMetadataBase implements DBPrivilegeMetadata {
    public DBPrivilegeMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getPrivilegeName() throws SQLException {
        return getString("PRIVILEGE_NAME");
    }
}
