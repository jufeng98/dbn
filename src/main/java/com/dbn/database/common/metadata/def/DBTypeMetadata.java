package com.dbn.database.common.metadata.def;

import java.sql.SQLException;

public interface DBTypeMetadata extends DBProgramMetadata {

    String getTypeName() throws SQLException;

    String getTypeCode() throws SQLException;

    String getSupertypeOwner() throws SQLException;

    String getSupertypeName() throws SQLException;

    boolean isCollection() throws SQLException;

    DBDataTypeMetadata getDataType();

    String getPackageName() throws SQLException;
}
