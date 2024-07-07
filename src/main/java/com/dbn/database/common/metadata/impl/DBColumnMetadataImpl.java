package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBColumnMetadata;
import com.dbn.database.common.metadata.def.DBDataTypeMetadata;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public class DBColumnMetadataImpl extends DBObjectMetadataBase implements DBColumnMetadata {
    private final DBDataTypeMetadata dataType;

    public DBColumnMetadataImpl(ResultSet resultSet) {
        super(resultSet);
        dataType = new DBDataTypeMetadataImpl(resultSet);
    }

    public String getColumnName() throws SQLException {
        return getString("COLUMN_NAME");
    }

    @Override
    public String getColumnComment() throws SQLException {
        return getString("COLUMN_COMMENT");
    }

    public String getDatasetName() throws SQLException {
        return getString("DATASET_NAME");
    }

    public boolean isPrimaryKey() throws SQLException {
        return isYesFlag("IS_PRIMARY_KEY");
    }

    public boolean isForeignKey() throws SQLException {
        return isYesFlag("IS_FOREIGN_KEY");
    }

    public boolean isUniqueKey() throws SQLException {
        return isYesFlag("IS_UNIQUE_KEY");
    }

    public boolean isIdentity() throws SQLException {
        return isYesFlag("IS_IDENTITY");
    }

    public boolean isNullable() throws SQLException {
        return isYesFlag("IS_NULLABLE");
    }

    public boolean isHidden() throws SQLException {
        return isYesFlag("IS_HIDDEN");
    }

    public short getPosition() throws SQLException {
        return resultSet.getShort("POSITION");
    }
}
