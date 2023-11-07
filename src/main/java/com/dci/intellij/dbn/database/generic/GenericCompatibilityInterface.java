package com.dci.intellij.dbn.database.generic;

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
                CONSOLE,
                SCHEMA,
                TABLE,
                VIEW,
                COLUMN,
                CONSTRAINT,
                INDEX,
                TRIGGER,
                FUNCTION,
                PROCEDURE,
                ARGUMENT);
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
