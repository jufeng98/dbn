package com.dbn.database.generic;

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

public class GenericCompatibilityInterface extends DatabaseCompatibilityInterfaceImpl {
    private static final QuoteDefinition IDENTIFIER_QUOTE_DEFINITION = new QuoteDefinition(new QuotePair('"', '"'));

    @Override
    public List<DatabaseFeature> getSupportedFeatures() {
        return Arrays.asList(
                OBJECT_SOURCE_EDITING,
                OBJECT_CHANGE_MONITORING,
                SESSION_CURRENT_SQL,
                CONNECTION_ERROR_RECOVERY,
                UPDATABLE_RESULT_SETS,
                CURRENT_SCHEMA,
                CONSTRAINT_MANIPULATION,
                READONLY_CONNECTIVITY);
    }

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
                DatabaseObjectTypeId.TRIGGER,
                DatabaseObjectTypeId.FUNCTION,
                DatabaseObjectTypeId.PROCEDURE,
                DatabaseObjectTypeId.ARGUMENT);
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
    public SessionStatus getSessionStatus(String statusName) {
        return Strings.isEmpty(statusName) ? SessionStatus.INACTIVE : SessionStatus.ACTIVE;
    }

    @Override
    public String getExplainPlanStatementPrefix() {
        return null;
    }
}
