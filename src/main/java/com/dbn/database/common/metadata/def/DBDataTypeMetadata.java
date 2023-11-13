package com.dbn.database.common.metadata.def;

import java.sql.SQLException;

public interface DBDataTypeMetadata {

    String getDataTypeName() throws SQLException;

    String getDeclaredTypeName() throws SQLException;

    String getDeclaredTypeOwner() throws SQLException;

    String getDeclaredTypeProgram() throws SQLException;

    long getDataLength() throws SQLException;

    int getDataPrecision() throws SQLException;

    int getDataScale() throws SQLException;

    boolean isSet() throws SQLException;

    DBDataTypeMetadata collection();
}
