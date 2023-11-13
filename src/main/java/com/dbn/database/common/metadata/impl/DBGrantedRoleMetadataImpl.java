package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBGrantedRoleMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBGrantedRoleMetadataImpl extends DBObjectMetadataBase implements DBGrantedRoleMetadata {
    public DBGrantedRoleMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getGrantedRoleName() throws SQLException {
        return getString("GRANTED_ROLE_NAME");
    }

    @Override
    public String getRoleName() throws SQLException {
        return getString("ROLE_NAME");
    }

    @Override
    public String getUserName() throws SQLException {
        return getString("USER_NAME");
    }

    @Override
    public boolean isAdminOption() throws SQLException {
        return isYesFlag("IS_ADMIN_OPTION");
    }

    @Override
    public boolean isDefaultRole() throws SQLException {
        return isYesFlag("IS_DEFAULT_ROLE");
    }
}
