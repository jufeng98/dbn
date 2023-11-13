package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBArgumentMetadata;
import com.dbn.database.common.metadata.def.DBDataTypeMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBArgumentMetadataImpl extends DBObjectMetadataBase implements DBArgumentMetadata {
    private DBDataTypeMetadata dataType;

    public DBArgumentMetadataImpl(ResultSet resultSet) {
        super(resultSet);
        dataType = new DBDataTypeMetadataImpl(resultSet);
    }

    @Override
    public String getArgumentName() throws SQLException {
        return getString("ARGUMENT_NAME");
    }

    @Override
    public String getProgramName() throws SQLException {
        return getString("PROGRAM_NAME");
    }

    @Override
    public String getMethodName() throws SQLException {
        return getString("METHOD_NAME");
    }

    @Override
    public String getMethodType() throws SQLException {
        return getString("METHOD_TYPE");
    }

    @Override
    public String getInOut() throws SQLException {
        return getString("IN_OUT");
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
    public short getSequence() throws SQLException {
        return resultSet.getShort("SEQUENCE");
    }

    public DBDataTypeMetadata getDataType() {
        return dataType;
    }

}
