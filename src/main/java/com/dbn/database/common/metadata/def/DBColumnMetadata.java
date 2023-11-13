package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBColumnMetadata extends DBObjectMetadata {

    String getColumnName() throws SQLException;

    String getDatasetName() throws SQLException;

    boolean isPrimaryKey() throws SQLException;

    boolean isForeignKey() throws SQLException;

    boolean isUniqueKey() throws SQLException;

    boolean isIdentity() throws SQLException;

    boolean isNullable() throws SQLException;

    boolean isHidden() throws SQLException;

    short getPosition() throws SQLException;

    DBDataTypeMetadata getDataType();
}
