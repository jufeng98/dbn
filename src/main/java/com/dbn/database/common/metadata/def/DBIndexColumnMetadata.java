package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBIndexColumnMetadata extends DBObjectMetadata {

    String getIndexName() throws SQLException;

    String getColumnName() throws SQLException;

    String getTableName() throws SQLException;

}
