package com.dbn.database;

import com.dbn.common.property.PropertyHolderBase;
import com.dbn.common.util.TransientId;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseCompatibility extends PropertyHolderBase.IntStore<JdbcProperty> {

    private String identifierQuote;
    private final Map<TransientId, DatabaseActivityTrace> activityTraces = new ConcurrentHashMap<>();

    private DatabaseCompatibility() {}

    public static DatabaseCompatibility allFeatures() {
        DatabaseCompatibility compatibility = new DatabaseCompatibility();
        // mark all features as supported
        for (JdbcProperty property : JdbcProperty.values()) {
            if (property.isFeature()) {
                compatibility.set(property, true);
            }
        }
        return compatibility;
    }

    public static DatabaseCompatibility noFeatures() {
        return new DatabaseCompatibility();
    }

    public void markUnsupported(JdbcProperty feature) {
        set(feature, false);
    }

    public boolean isSupported(JdbcProperty feature) {
        return is(feature);
    };

    public DatabaseActivityTrace getActivityTrace(TransientId operationId) {
        return activityTraces.computeIfAbsent(operationId, id -> new DatabaseActivityTrace());
    }

    public String getIdentifierQuote() {
        return identifierQuote;
    }

    @Override
    protected JdbcProperty[] properties() {
        return JdbcProperty.VALUES;
    }

    public void read(DatabaseMetaData metaData) throws SQLException {
        String quoteString = metaData.getIdentifierQuoteString();
        identifierQuote = quoteString == null ? "" : quoteString.trim();

        //TODO JdbcProperty.SQL_DATASET_ALIASING (identify by database type?)
    }
}
