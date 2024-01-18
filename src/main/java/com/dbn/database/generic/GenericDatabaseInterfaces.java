package com.dbn.database.generic;

import com.dbn.connection.DatabaseType;
import com.dbn.database.common.DatabaseEnvironmentInterfaceImpl;
import com.dbn.database.common.DatabaseInterfacesBase;
import com.dbn.database.common.DatabaseNativeDataTypes;
import com.dbn.database.interfaces.*;
import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.sql.SQLLanguage;
import lombok.Getter;

public final class GenericDatabaseInterfaces extends DatabaseInterfacesBase {
    private final @Getter(lazy = true) DatabaseMessageParserInterface messageParserInterface = new GenericMessageParserInterface();
    private final @Getter(lazy = true) DatabaseCompatibilityInterface compatibilityInterface = new GenericCompatibilityInterface();
    private final @Getter(lazy = true) DatabaseEnvironmentInterface environmentInterface = new DatabaseEnvironmentInterfaceImpl();
    private final @Getter(lazy = true) DatabaseMetadataInterface metadataInterface = new GenericMetadataInterface(this);
    private final @Getter(lazy = true) DatabaseDataDefinitionInterface dataDefinitionInterface = new GenericDataDefinitionInterface(this);
    private final @Getter(lazy = true) DatabaseExecutionInterface executionInterface = new GenericExecutionInterface();
    private final @Getter(lazy = true) DatabaseNativeDataTypes nativeDataTypes = new GenericNativeDataTypes();

    public GenericDatabaseInterfaces() {
        //super(SQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.ISO92_SQL), null);
        // TODO ISO92 far from complete - fallback to SQLITE
        super(SQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.SQLITE_SQL), null);
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.GENERIC;
    }
}
