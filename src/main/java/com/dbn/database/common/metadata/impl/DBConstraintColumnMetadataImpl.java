package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBConstraintColumnMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConstraintColumnMetadataImpl extends DBObjectMetadataBase implements DBConstraintColumnMetadata {

    public DBConstraintColumnMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getConstraintName() throws SQLException {
        return getString("CONSTRAINT_NAME");
    }

    @Override
    public String getColumnName() throws SQLException {
        return getString("COLUMN_NAME");
    }

    public String getDatasetName() throws SQLException {
        return getString("DATASET_NAME");
    }

    public short getPosition() throws SQLException {
        return resultSet.getShort("POSITION");
    }
}
