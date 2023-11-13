package com.dbn.database.sqlite.adapter.rs;

import com.dbn.common.cache.CacheKey;
import com.dbn.database.sqlite.adapter.SqliteMetadataResultSet;
import com.dbn.database.sqlite.adapter.SqliteMetadataResultSetRow;
import com.dbn.database.sqlite.adapter.SqliteRawMetaData.TableNames;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * DATASET_NAME
 */

public abstract class SqliteDatasetNamesResultSet extends SqliteMetadataResultSet<SqliteDatasetNamesResultSet.Dataset> {
    protected String ownerName;
    protected SqliteDatasetNamesResultSet(String ownerName) throws SQLException {
        this.ownerName = ownerName;
        TableNames tableNames = getTableNames();

        for (TableNames.Row row : tableNames.getRows()) {
            Dataset element = new Dataset();
            element.datasetName = row.getName();
            add(element);
        }
    }

    private TableNames getTableNames() throws SQLException {
        return cache().get(
                CacheKey.key(ownerName, "DATASET_NAMES"),
                () -> new TableNames(loadTableNames()));
    }

    protected abstract ResultSet loadTableNames() throws SQLException;

    @Override
    public String getString(String columnLabel) throws SQLException {
        Dataset element = current();
        if (Objects.equals(columnLabel, "DATASET_NAME")) {
            return element.datasetName;
        }
        return null;
    }

    static class Dataset implements SqliteMetadataResultSetRow<Dataset> {
        private String datasetName;

        @Override
        public String identifier() {
            return datasetName;
        }
    }
}
