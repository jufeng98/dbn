package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBProgramMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBProgramMetadataImpl extends DBObjectMetadataBase implements DBProgramMetadata {

    DBProgramMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getSpecValid() throws SQLException {
        return getString("IS_SPEC_VALID");
    }

    public String getBodyValid() throws SQLException {
        return getString("IS_BODY_VALID");
    }

    public String getSpecDebug() throws SQLException {
        return getString("IS_SPEC_DEBUG");
    }

    public String getBodyDebug() throws SQLException {
        return getString("IS_BODY_DEBUG");
    }
}
