package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBMethodMetadata extends DBObjectMetadata {

    boolean isDeterministic() throws SQLException;

    boolean isValid() throws SQLException;

    boolean isDebug() throws SQLException;

    short getOverload() throws SQLException;

    short getPosition() throws SQLException;

    String getLanguage() throws SQLException;

    String getTypeName() throws SQLException;

    String getPackageName() throws SQLException;
}
