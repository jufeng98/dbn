package com.dbn.database.common;

import com.dbn.database.interfaces.DatabaseDebuggerInterface;
import com.dbn.database.interfaces.DatabaseInterfaces;
import com.dbn.language.common.DBLanguage;
import com.dbn.language.common.DBLanguageDialect;
import com.dbn.language.psql.PSQLLanguage;
import com.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dbn.language.sql.SQLLanguage;
import com.dbn.language.sql.dialect.SQLLanguageDialect;
import org.jetbrains.annotations.Nullable;

public abstract class DatabaseInterfacesBase implements DatabaseInterfaces {
    private final SQLLanguageDialect sqlLanguageDialect;
    private final PSQLLanguageDialect psqlLanguageDialect;

    protected DatabaseInterfacesBase(SQLLanguageDialect sqlLanguageDialect, @Nullable PSQLLanguageDialect psqlLanguageDialect) {
        this.sqlLanguageDialect = sqlLanguageDialect;
        this.psqlLanguageDialect = psqlLanguageDialect;
    }

    @Nullable
    @Override
    public DBLanguageDialect getLanguageDialect(DBLanguage<?> language) {
        if (language == SQLLanguage.INSTANCE) return sqlLanguageDialect;
        if (language == PSQLLanguage.INSTANCE) return psqlLanguageDialect;
        return null;
    }

    @Override
    public void reset() {
        getMetadataInterface().reset();
        getDataDefinitionInterface().reset();
        DatabaseDebuggerInterface debuggerInterface = getDebuggerInterface();
        if (debuggerInterface != null) debuggerInterface.reset();
    }
}
