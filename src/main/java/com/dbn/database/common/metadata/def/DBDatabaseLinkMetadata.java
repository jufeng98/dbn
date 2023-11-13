package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBDatabaseLinkMetadata extends DBObjectMetadata {

    String getDblinkName() throws SQLException;

    String getUserName() throws SQLException;

    String getHost() throws SQLException;
}
