package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBNestedTableMetadata extends DBObjectMetadata {

    String getNestedTableName() throws SQLException;

    String getTableName() throws SQLException;

    String getDeclaredTypeName() throws SQLException;

    String getDeclaredTypeOwner() throws SQLException;
}
