package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBGrantedPrivilegeMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBGrantedPrivilegeMetadataImpl extends DBObjectMetadataBase implements DBGrantedPrivilegeMetadata {
    public DBGrantedPrivilegeMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getGrantedPrivilegeName() throws SQLException {
        return getString("GRANTED_PRIVILEGE_NAME");
    }

    @Override
    public String getUserName() throws SQLException {
        return getString("USER_NAME");
    }

    @Override
    public String getRoleName() throws SQLException {
        return getString("ROLE_NAME");
    }

    @Override
    public boolean isAdminOption() throws SQLException {
        return isYesFlag("IS_ADMIN_OPTION");
    }
}
