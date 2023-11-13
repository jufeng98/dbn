package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.def.DBProcedureMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBProcedureMetadataImpl extends DBMethodMetadataImpl implements DBProcedureMetadata {
    public DBProcedureMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getProcedureName() throws SQLException {
        return getString("PROCEDURE_NAME");
    }
}
