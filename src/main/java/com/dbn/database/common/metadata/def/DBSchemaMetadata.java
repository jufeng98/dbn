package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBSchemaMetadata extends DBObjectMetadata {

    String getSchemaName() throws SQLException;

    boolean isPublic() throws SQLException;

    boolean isSystem() throws SQLException;

    boolean isEmpty() throws SQLException;
}
