package com.dci.intellij.dbn.database.postgres;

import com.dci.intellij.dbn.common.util.Strings;
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

public class PostgresCompatibilityInterface extends DatabaseCompatibilityInterfaceImpl {

    public static final QuoteDefinition IDENTIFIER_QUOTE_DEFINITION = new QuoteDefinition(new QuotePair('"', '"'));

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
                //DATABASE_TRIGGER,
                FUNCTION,
                ARGUMENT,
                SEQUENCE,
                SYSTEM_PRIVILEGE,
                GRANTED_PRIVILEGE);
    }

    @Override
    public List<DatabaseFeature> getSupportedFeatures() {
        return Arrays.asList(
                SESSION_BROWSING,
                SESSION_KILL,
                SESSION_CURRENT_SQL,
                UPDATABLE_RESULT_SETS,
                OBJECT_SOURCE_EDITING,
                CURRENT_SCHEMA,
                CONSTRAINT_MANIPULATION,
                READONLY_CONNECTIVITY);
    }

    @Override
    public SessionStatus getSessionStatus(String statusName) {
        if (Strings.isEmpty(statusName)) return SessionStatus.INACTIVE;
        if (statusName.equalsIgnoreCase("active")) return SessionStatus.ACTIVE;
        if (statusName.equalsIgnoreCase("idle")) return SessionStatus.INACTIVE;
        return SessionStatus.SNIPED;
    }

    @Override
    public QuoteDefinition getIdentifierQuotes() {
        return IDENTIFIER_QUOTE_DEFINITION;
    }

    @Override
    public String getDefaultAlternativeStatementDelimiter() {
        return null;
    }

    @Override
    public String getExplainPlanStatementPrefix() {
        return "explain analyze verbose ";
    }
}
