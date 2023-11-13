package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBTypeAttributeMetadata extends DBObjectMetadata {

    String getAttributeName() throws SQLException;

    String getTypeName() throws SQLException;

    short getPosition() throws SQLException;

    DBDataTypeMetadata getDataType();
}
