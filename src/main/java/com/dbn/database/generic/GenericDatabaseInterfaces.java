package com.dbn.database.generic;

import com.dbn.connection.DatabaseType;
import com.dbn.database.common.DatabaseEnvironmentInterfaceImpl;
import com.dbn.database.common.DatabaseInterfacesBase;
import com.dbn.database.common.DatabaseNativeDataTypes;
import com.dbn.database.interfaces.*;
import com.dbn.database.interfaces.*;
import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.sql.SQLLanguage;
import lombok.Getter;

@Getter
public final class GenericDatabaseInterfaces extends DatabaseInterfacesBase {
    private final DatabaseMessageParserInterface messageParserInterface = new GenericMessageParserInterface();
    private final DatabaseCompatibilityInterface compatibilityInterface = new GenericCompatibilityInterface();
    private final DatabaseEnvironmentInterface environmentInterface = new DatabaseEnvironmentInterfaceImpl();
    private final DatabaseMetadataInterface metadataInterface = new GenericMetadataInterface(this);
    private final DatabaseDataDefinitionInterface dataDefinitionInterface = new GenericDataDefinitionInterface(this);
    private final DatabaseExecutionInterface executionInterface = new GenericExecutionInterface();
    private final DatabaseNativeDataTypes nativeDataTypes = new GenericNativeDataTypes();

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
