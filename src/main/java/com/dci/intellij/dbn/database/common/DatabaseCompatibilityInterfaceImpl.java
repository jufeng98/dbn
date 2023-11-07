package com.dci.intellij.dbn.database.common;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.DatabaseAttachmentHandler;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.database.DatabaseCompatibility;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.database.DatabaseObjectTypeId;
import com.dci.intellij.dbn.database.JdbcProperty;
import com.dci.intellij.dbn.database.interfaces.DatabaseCompatibilityInterface;
import com.dci.intellij.dbn.language.common.QuotePair;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashSet;
import java.util.Set;

import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

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
