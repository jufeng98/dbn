package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBGrantedRoleMetadata extends DBObjectMetadata {

    String getRoleName() throws SQLException;

    String getUserName() throws SQLException;

    String getGrantedRoleName() throws SQLException;

    boolean isAdminOption() throws SQLException;

    boolean isDefaultRole() throws SQLException;
}
