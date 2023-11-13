package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBIndexMetadata extends DBObjectMetadata {

    String getIndexName() throws SQLException;

    String getTableName() throws SQLException;

    boolean isUnique() throws SQLException;

    boolean isValid() throws SQLException;

}
