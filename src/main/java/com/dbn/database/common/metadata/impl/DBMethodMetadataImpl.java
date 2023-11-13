package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBMethodMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBMethodMetadataImpl extends DBObjectMetadataBase implements DBMethodMetadata {
    DBMethodMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public boolean isDeterministic() throws SQLException {
        return isYesFlag("IS_DETERMINISTIC");
    }

    @Override
    public boolean isValid() throws SQLException {
        return isYesFlag("IS_VALID");
    }

    @Override
    public boolean isDebug() throws SQLException {
        return isYesFlag("IS_DEBUG");
    }

    @Override
    public short getOverload() throws SQLException {
        return resultSet.getShort("OVERLOAD");
    }

    @Override
    public short getPosition() throws SQLException {
        return resultSet.getShort("POSITION");
    }

    @Override
    public String getLanguage() throws SQLException {
        return getString("LANGUAGE");
    }

    @Override
    public String getTypeName() throws SQLException {
        return getString("TYPE_NAME");
    }

    @Override
    public String getPackageName() throws SQLException {
        return getString("PACKAGE_NAME");
    }
}
