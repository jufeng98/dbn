package com.dbn.database.common;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.DatabaseAttachmentHandler;
import com.dbn.data.sorting.SortDirection;
import com.dbn.database.DatabaseCompatibility;
import com.dbn.database.DatabaseFeature;
import com.dbn.database.DatabaseObjectTypeId;
import com.dbn.database.JdbcProperty;
import com.dbn.database.interfaces.DatabaseCompatibilityInterface;
import com.dbn.language.common.QuotePair;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashSet;
import java.util.Set;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
public abstract class DatabaseCompatibilityInterfaceImpl implements DatabaseCompatibilityInterface {
    private final Set<DatabaseObjectTypeId> supportedObjectTypes = new HashSet<>(getSupportedObjectTypes());
    private final Set<DatabaseFeature> supportedFeatures = new HashSet<>(getSupportedFeatures());

    @Override
    public boolean supportsObjectType(DatabaseObjectTypeId objectTypeId) {
        return supportedObjectTypes.contains(objectTypeId);
    }

    @Override
    public boolean supportsFeature(DatabaseFeature feature) {
        return supportedFeatures.contains(feature);
    }

    @Override
    public QuotePair getDefaultIdentifierQuotes() {
        return getIdentifierQuotes().getDefaultQuotes();
    }

    @Override
    @Nullable
    public String getDatabaseLogName() {
        return null;
    }

    @Override
    public String getOrderByClause(String columnName, SortDirection sortDirection, boolean nullsFirst) {
        return columnName + " " + sortDirection.getSqlToken() + " nulls " + (nullsFirst ? " first" : " last");
    }

    @Override
    public String getForUpdateClause() {
        return " for update";
    }

    @Override
    public String getSessionBrowserColumnName(String columnName) {
        return columnName;
    }

    @Override
    @Nullable
    public DatabaseAttachmentHandler getDatabaseAttachmentHandler() {
        return null;
    };

    public <T> T attemptFeatureInvocation(JdbcProperty feature, Callable<T> invoker) throws SQLException {
        ConnectionHandler connection = ConnectionHandler.local();
        DatabaseCompatibility compatibility = connection.getCompatibility();
        try {
            if (compatibility.isSupported(feature)) {
                return invoker.call();
            }
        } catch (SQLFeatureNotSupportedException | AbstractMethodError e) {
            conditionallyLog(e);
            log.warn("JDBC feature not supported " + feature + " (" + e.getMessage() + ")");
            compatibility.markUnsupported(feature);
        }
        return null;
    }
}
