package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBUserMetadata extends DBObjectMetadata {

    String getUserName() throws SQLException;

    boolean isExpired() throws SQLException;

    boolean isLocked() throws SQLException;
}
