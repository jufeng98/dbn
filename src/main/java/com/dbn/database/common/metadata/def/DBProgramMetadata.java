package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBProgramMetadata extends DBObjectMetadata {

    String getSpecValid() throws SQLException;

    String getBodyValid() throws SQLException;

    String getSpecDebug() throws SQLException;

    String getBodyDebug() throws SQLException;
}
