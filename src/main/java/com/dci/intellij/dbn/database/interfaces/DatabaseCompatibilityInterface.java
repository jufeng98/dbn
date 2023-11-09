package com.dci.intellij.dbn.database.interfaces;

import com.dci.intellij.dbn.connection.DatabaseAttachmentHandler;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.database.DatabaseObjectTypeId;
import com.dci.intellij.dbn.database.JdbcProperty;
import com.dci.intellij.dbn.editor.session.SessionStatus;
import com.dci.intellij.dbn.language.common.QuoteDefinition;
import com.dci.intellij.dbn.language.common.QuotePair;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseCompatibilityInterface extends DatabaseInterface {
    List<DatabaseObjectTypeId> getSupportedObjectTypes();

    List<DatabaseFeature> getSupportedFeatures();

    boolean supportsObjectType(DatabaseObjectTypeId objectTypeId);

    boolean supportsFeature(DatabaseFeature feature);

    QuoteDefinition getIdentifierQuotes();

    QuotePair getDefaultIdentifierQuotes();

    @Nullable String getDatabaseLogName();

    String getDefaultAlternativeStatementDelimiter();

    String getOrderByClause(String columnName, SortDirection sortDirection, boolean nullsFirst);

    String getForUpdateClause();

    String getSessionBrowserColumnName(String columnName);

    SessionStatus getSessionStatus(String statusName);

    String getExplainPlanStatementPrefix();

    @Nullable DatabaseAttachmentHandler getDatabaseAttachmentHandler();

    <T> T attemptFeatureInvocation(JdbcProperty feature, Callable<T> invoker) throws SQLException;
}
