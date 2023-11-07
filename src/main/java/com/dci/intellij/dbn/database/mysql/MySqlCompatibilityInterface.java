package com.dci.intellij.dbn.database.mysql;

import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.database.DatabaseObjectTypeId;
import com.dci.intellij.dbn.database.common.DatabaseCompatibilityInterfaceImpl;
import com.dci.intellij.dbn.editor.session.SessionStatus;
import com.dci.intellij.dbn.language.common.QuoteDefinition;
import com.dci.intellij.dbn.language.common.QuotePair;

import java.util.Arrays;
import java.util.List;

import static com.dci.intellij.dbn.database.DatabaseFeature.*;
import static com.dci.intellij.dbn.database.DatabaseObjectTypeId.*;

public class MySqlCompatibilityInterface extends DatabaseCompatibilityInterfaceImpl {
    private static final QuoteDefinition IDENTIFIER_QUOTE_DEFINITION = new QuoteDefinition(new QuotePair('`', '`'));

    @Override
    public List<DatabaseObjectTypeId> getSupportedObjectTypes() {
        return Arrays.asList(
                CONSOLE,
                CHARSET,
                USER,
                SCHEMA,
                TABLE,
                VIEW,
                COLUMN,
                CONSTRAINT,
                INDEX,
                DATASET_TRIGGER,
                FUNCTION,
                PROCEDURE,
                ARGUMENT,
                SYSTEM_PRIVILEGE,
                GRANTED_PRIVILEGE);
    }

    @Override
    public List<DatabaseFeature> getSupportedFeatures() {
        return Arrays.asList(
                SESSION_BROWSING,
                SESSION_KILL,
                OBJECT_CHANGE_MONITORING,
                OBJECT_SOURCE_EDITING,
                UPDATABLE_RESULT_SETS,
                CURRENT_SCHEMA,
                CONSTRAINT_MANIPULATION,
                READONLY_CONNECTIVITY);
    }

    @Override
    public QuoteDefinition getIdentifierQuotes() {
        return IDENTIFIER_QUOTE_DEFINITION;
    }

    @Override
    public String getDefaultAlternativeStatementDelimiter() {
        return "$$";
    }

    @Override
    public String getOrderByClause(String columnName, SortDirection sortDirection, boolean nullsFirst) {
        nullsFirst = (nullsFirst && sortDirection == SortDirection.ASCENDING) || (!nullsFirst && sortDirection == SortDirection.DESCENDING);
        return "(" + columnName + " is" + (nullsFirst ? "" : " not") + " null), " + columnName + " " + sortDirection.getSqlToken();
    }

    @Override
    public String getExplainPlanStatementPrefix() {
        return null;
    }

    @Override
    public String getSessionBrowserColumnName(String columnName) {
        if (columnName.equalsIgnoreCase("id")) return "SESSION_ID";
        if (columnName.equalsIgnoreCase("user")) return "USER";
        if (columnName.equalsIgnoreCase("host")) return "HOST";
        if (columnName.equalsIgnoreCase("db")) return "DATABASE";
        if (columnName.equalsIgnoreCase("command")) return "COMMAND";
        if (columnName.equalsIgnoreCase("time")) return "TIME";
        if (columnName.equalsIgnoreCase("state")) return "STATUS";
        if (columnName.equalsIgnoreCase("info")) return "CLIENT_INFO";
        return super.getSessionBrowserColumnName(columnName);
    }

    @Override
    public SessionStatus getSessionStatus(String statusName) {
        if (Strings.isEmpty(statusName)) return SessionStatus.INACTIVE;
        else return SessionStatus.ACTIVE;
    }
}
