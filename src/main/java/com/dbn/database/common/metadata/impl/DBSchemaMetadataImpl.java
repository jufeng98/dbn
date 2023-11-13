package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBSchemaMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBSchemaMetadataImpl extends DBObjectMetadataBase implements DBSchemaMetadata {
    public DBSchemaMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getSchemaName() throws SQLException {
        return getString("SCHEMA_NAME");
    }

    public boolean isPublic() throws SQLException {
        return isYesFlag("IS_PUBLIC");
    }

    public boolean isSystem() throws SQLException {
        return isYesFlag("IS_SYSTEM");
    }

    public boolean isEmpty() throws SQLException {
        return isYesFlag("IS_EMPTY");
    }

}

