package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBArgumentMetadata extends DBObjectMetadata {

    String getArgumentName() throws SQLException;

    String getProgramName() throws SQLException;

    String getMethodName() throws SQLException;

    String getMethodType() throws SQLException;

    String getInOut() throws SQLException;

    short getOverload() throws SQLException;

    short getPosition() throws SQLException;

    short getSequence() throws SQLException;

    DBDataTypeMetadata getDataType();
}
