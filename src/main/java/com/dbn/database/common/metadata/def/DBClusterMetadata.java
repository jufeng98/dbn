package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBClusterMetadata extends DBObjectMetadata {

    String getClusterName() throws SQLException;

}
