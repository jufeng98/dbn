package com.dbn.database.sqlite.adapter.rs;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.database.common.util.ResultSetReader;
import com.dbn.database.sqlite.adapter.SqliteMetadataResultSet;
import com.dbn.database.sqlite.adapter.SqliteMetadataResultSetRow;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public abstract class SqliteDatasetInfoResultSetStub<T extends SqliteMetadataResultSetRow> extends SqliteMetadataResultSet<T> {
    private final DBNConnection connection;
    protected String ownerName;

    SqliteDatasetInfoResultSetStub(final String ownerName, SqliteDatasetNamesResultSet datasetNames, DBNConnection connection) throws SQLException {
        this.connection = connection;
        this.ownerName = ownerName;
        new ResultSetReader(datasetNames) {
            @Override
            protected void processRow(ResultSet resultSet) throws SQLException {
                String datasetName = resultSet.getString("DATASET_NAME");
                init(ownerName, datasetName);
            }
        };
    }

    SqliteDatasetInfoResultSetStub(String ownerName, String datasetName, DBNConnection connection) throws SQLException {
        this.connection = connection;
        this.ownerName = ownerName;
        init(ownerName, datasetName);
    }

    protected abstract void init(String ownerName, String datasetName) throws SQLException;
}
