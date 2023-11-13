package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBObjectDependencyMetadata extends DBObjectMetadata {

    String getObjectName() throws SQLException;

    String getObjectOwner() throws SQLException;

    String getObjectType() throws SQLException;
}
