package com.dci.intellij.dbn.database.interfaces;

import com.dci.intellij.dbn.connection.DatabaseType;
import com.dci.intellij.dbn.database.common.DatabaseNativeDataTypes;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import org.jetbrains.annotations.Nullable;

public interface DatabaseInterfaces {

    DatabaseType getDatabaseType();

    @Nullable
    DBLanguageDialect getLanguageDialect(DBLanguage<?> language);

    DatabaseNativeDataTypes getNativeDataTypes();

    DatabaseMessageParserInterface getMessageParserInterface();

    DatabaseEnvironmentInterface getEnvironmentInterface();

    DatabaseCompatibilityInterface getCompatibilityInterface();

    DatabaseMetadataInterface getMetadataInterface();

    DatabaseDataDefinitionInterface getDataDefinitionInterface();

    DatabaseExecutionInterface getExecutionInterface();

    default DatabaseDebuggerInterface getDebuggerInterface() {
        return null;
    }

    void reset();
}
