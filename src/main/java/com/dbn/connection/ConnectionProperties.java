package com.dbn.connection;

import com.dbn.common.property.PropertyHolderBase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionProperties extends PropertyHolderBase.IntStore<ConnectionProperty> {

    public ConnectionProperties(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        set(ConnectionProperty.RS_TYPE_SCROLL_INSENSITIVE, metaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
        set(ConnectionProperty.RS_TYPE_FORWARD_ONLY,       metaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
    }

    @Override
    protected ConnectionProperty[] properties() {
        return ConnectionProperty.VALUES;
    }
}
