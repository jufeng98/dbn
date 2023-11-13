package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBSynonymMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBSynonymMetadataImpl extends DBObjectMetadataBase implements DBSynonymMetadata {

    public DBSynonymMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getSynonymName() throws SQLException {
        return getString("SYNONYM_NAME");
    }

    @Override
    public String getUnderlyingObjectOwner() throws SQLException {
        return getString("OBJECT_OWNER");
    }

    @Override
    public String getUnderlyingObjectName() throws SQLException {
        return getString("OBJECT_NAME");
    }

    @Override
    public String getUnderlyingObjectType() throws SQLException {
        return getString("OBJECT_TYPE");
    }

    @Override
    public boolean isValid() throws SQLException {
        return isYesFlag("IS_VALID");
    }
}
