package com.dbn.database.postgres;

import com.dbn.common.util.Strings;
import com.dbn.database.DatabaseObjectTypeId;
import com.dbn.database.DatabaseFeature;
import com.dbn.database.common.DatabaseCompatibilityInterfaceImpl;
import com.dbn.editor.session.SessionStatus;
import com.dbn.language.common.QuoteDefinition;
import com.dbn.language.common.QuotePair;

import java.util.Arrays;
import java.util.List;

import static com.dbn.database.DatabaseFeature.*;

public class PostgresCompatibilityInterface extends DatabaseCompatibilityInterfaceImpl {

    public static final QuoteDefinition IDENTIFIER_QUOTE_DEFINITION = new QuoteDefinition(new QuotePair('"', '"'));

    @Override
    public List<DatabaseObjectTypeId> getSupportedObjectTypes() {
        return Arrays.asList(
                DatabaseObjectTypeId.CONSOLE,
                DatabaseObjectTypeId.CHARSET,
                DatabaseObjectTypeId.USER,
                DatabaseObjectTypeId.SCHEMA,
                DatabaseObjectTypeId.TABLE,
                DatabaseObjectTypeId.VIEW,
                DatabaseObjectTypeId.COLUMN,
                DatabaseObjectTypeId.CONSTRAINT,
                DatabaseObjectTypeId.INDEX,
                DatabaseObjectTypeId.DATASET_TRIGGER,
                //DATABASE_TRIGGER,
                DatabaseObjectTypeId.FUNCTION,
                DatabaseObjectTypeId.ARGUMENT,
                DatabaseObjectTypeId.SEQUENCE,
                DatabaseObjectTypeId.SYSTEM_PRIVILEGE,
                DatabaseObjectTypeId.GRANTED_PRIVILEGE);
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
