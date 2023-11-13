package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBRoleMetadata extends DBObjectMetadata {

    String getRoleName() throws SQLException;
}
