package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.def.DBDataTypeMetadata;
import com.dbn.database.common.metadata.def.DBTypeMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DBTypeMetadataImpl extends DBProgramMetadataImpl implements DBTypeMetadata {
    private final DBDataTypeMetadata dataType;

    public DBTypeMetadataImpl(ResultSet resultSet) {
        super(resultSet);
        dataType = new DBDataTypeMetadataImpl(resultSet);
    }

    public String getTypeName() throws SQLException {
        return getString("TYPE_NAME");
    }

    public String getTypeCode() throws SQLException {
        return getString("TYPECODE");
    }

    public String getSupertypeOwner() throws SQLException {
        return getString("SUPERTYPE_OWNER");
    }

    public String getSupertypeName() throws SQLException {
        return getString("SUPERTYPE_NAME");
    }

    public boolean isCollection() throws SQLException {
        String typeCode = getTypeCode();
        return Objects.equals(typeCode, "COLLECTION");
    }

    @Override
    public String getPackageName() throws SQLException {
        return getString("PACKAGE_NAME");
    }

    @Override
    public DBDataTypeMetadata getDataType() {
        return dataType;
    }
}
