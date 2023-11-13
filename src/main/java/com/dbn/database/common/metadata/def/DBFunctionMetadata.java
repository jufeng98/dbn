package com.dbn.database.common.metadata.def;

import java.sql.SQLException;

public interface DBFunctionMetadata extends DBMethodMetadata {

    String getFunctionName() throws SQLException;
}
