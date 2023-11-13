package com.dbn.database.sqlite;

import com.dbn.common.util.Strings;
import com.dbn.connection.DatabaseAttachmentHandler;
import com.dbn.data.sorting.SortDirection;
import com.dbn.database.DatabaseObjectTypeId;
import com.dbn.database.DatabaseFeature;
import com.dbn.database.common.DatabaseCompatibilityInterfaceImpl;
import com.dbn.editor.session.SessionStatus;
import com.dbn.language.common.QuoteDefinition;
import com.dbn.language.common.QuotePair;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static com.dbn.database.DatabaseFeature.CONNECTION_ERROR_RECOVERY;
import static com.dbn.database.DatabaseFeature.OBJECT_SOURCE_EDITING;
import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

class SqliteCompatibilityInterface extends DatabaseCompatibilityInterfaceImpl {

    private static final QuoteDefinition IDENTIFIER_QUOTE_DEFINITION = new QuoteDefinition(
            new QuotePair('"', '"'),
            new QuotePair('[', ']'),
            new QuotePair('`', '`'));

    @Override
    public List<DatabaseObjectTypeId> getSupportedObjectTypes() {
        return Arrays.asList(
                DatabaseObjectTypeId.CONSOLE,
                DatabaseObjectTypeId.SCHEMA,
                DatabaseObjectTypeId.TABLE,
                DatabaseObjectTypeId.VIEW,
                DatabaseObjectTypeId.COLUMN,
                DatabaseObjectTypeId.CONSTRAINT,
                DatabaseObjectTypeId.INDEX,
                DatabaseObjectTypeId.SAVEPOINT,
                DatabaseObjectTypeId.DATASET_TRIGGER);
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
