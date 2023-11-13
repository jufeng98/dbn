package com.dbn.database.common.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public abstract class DBObjectMetadataBase {
    protected ResultSet resultSet;

    public DBObjectMetadataBase(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    protected String getString(String columnLabel) throws SQLException {
        String string = resultSet.getString(columnLabel);
        return string == null ? null : string.intern();
    }

    protected boolean isYesFlag(String columnLabel) throws SQLException {
        return Objects.equals(getString(columnLabel), "Y");
    }
}
