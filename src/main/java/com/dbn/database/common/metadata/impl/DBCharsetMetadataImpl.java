package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBCharsetMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCharsetMetadataImpl extends DBObjectMetadataBase implements DBCharsetMetadata {

    public DBCharsetMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getCharsetName() throws SQLException {
        return getString("CHARSET_NAME");
    }

    @Override
    public String getDisplayName() throws SQLException {
        return getString("DISPLAY_NAME");
    }

    @Override
    public boolean isDeprecated() throws SQLException {
        return resultSet.getBoolean("IS_DEPRECATED");
    }

    @Override
    public short getMaxLength() throws SQLException {
        return resultSet.getShort("MAX_LENGTH");
    }


}
