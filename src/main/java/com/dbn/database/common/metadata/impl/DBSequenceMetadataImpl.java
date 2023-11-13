package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBSequenceMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBSequenceMetadataImpl extends DBObjectMetadataBase implements DBSequenceMetadata {

    public DBSequenceMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    public String getSequenceName() throws SQLException {
        return getString("SEQUENCE_NAME");
    }
}
