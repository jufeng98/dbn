package com.dci.intellij.dbn.database.sqlite;

import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.DatabaseAttachmentHandler;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.database.DatabaseObjectTypeId;
import com.dci.intellij.dbn.database.common.DatabaseCompatibilityInterfaceImpl;
import com.dci.intellij.dbn.editor.session.SessionStatus;
import com.dci.intellij.dbn.language.common.QuoteDefinition;
import com.dci.intellij.dbn.language.common.QuotePair;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static com.dci.intellij.dbn.database.DatabaseFeature.CONNECTION_ERROR_RECOVERY;
import static com.dci.intellij.dbn.database.DatabaseFeature.OBJECT_SOURCE_EDITING;
import static com.dci.intellij.dbn.database.DatabaseObjectTypeId.*;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

class SqliteCompatibilityInterface extends DatabaseCompatibilityInterfaceImpl {

    private static final QuoteDefinition IDENTIFIER_QUOTE_DEFINITION = new QuoteDefinition(
            new QuotePair('"', '"'),
            new QuotePair('[', ']'),
            new QuotePair('`', '`'));

    @Override
    public List<DatabaseObjectTypeId> getSupportedObjectTypes() {
        return Arrays.asList(
                CONSOLE,
                SCHEMA,
                TABLE,
                VIEW,
                COLUMN,
                CONSTRAINT,
                INDEX,
                SAVEPOINT,
                DATASET_TRIGGER);
    }

    @Override
    public List<DatabaseFeature> getSupportedFeatures() {
        return Arrays.asList(
                CONNECTION_ERROR_RECOVERY,
                OBJECT_SOURCE_EDITING);
    }

    @Override
    public QuoteDefinition getIdentifierQuotes() {
        return IDENTIFIER_QUOTE_DEFINITION;
    }

    @Override
    public String getDefaultAlternativeStatementDelimiter() {
        return ";";
    }

    @Override
    public String getOrderByClause(String columnName, SortDirection sortDirection, boolean nullsFirst) {
        nullsFirst = (nullsFirst && sortDirection == SortDirection.ASCENDING) || (!nullsFirst && sortDirection == SortDirection.DESCENDING);
        return "(" + columnName + " is" + (nullsFirst ? "" : " not") + " null), " + columnName + " " + sortDirection.getSqlToken();
    }

    @Override
    public String getForUpdateClause() {
        return "";
    }

    @Override
    public String getExplainPlanStatementPrefix() {
        return null;
    }

    @Override
    public SessionStatus getSessionStatus(String statusName) {
        if (Strings.isEmpty(statusName)) return SessionStatus.INACTIVE;
        else return SessionStatus.ACTIVE;
    }

    @Nullable
    @Override
    public DatabaseAttachmentHandler getDatabaseAttachmentHandler() {
        return (connection, filePath, schemaName) -> {
            //setAutoCommit(connection, false);
            try {
                //connection.rollback();
                Statement statement = connection.createStatement();
/*
                try {
                    statement.execute("end transaction");
                } catch (SQLException ignore) {}
*/
                statement.executeUpdate("attach database '" + filePath + "' as \"" + schemaName + "\"");
            } finally {
                //setAutoCommit(connection, true);
            }
        };
    }

    private void setAutoCommit(Connection connection, boolean autoCommit) throws SQLException {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            conditionallyLog(e);
            if (connection.getAutoCommit() != autoCommit) {
                throw e;
            }

        }
    }
}
