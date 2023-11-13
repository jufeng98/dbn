package com.dbn.database.postgres;

import com.dbn.connection.DatabaseType;
import com.dbn.database.common.DatabaseEnvironmentInterfaceImpl;
import com.dbn.database.common.DatabaseInterfacesBase;
import com.dbn.database.common.DatabaseNativeDataTypes;
import com.dbn.database.interfaces.*;
import com.dbn.database.interfaces.*;
import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.psql.PSQLLanguage;
import com.dbn.language.sql.SQLLanguage;
import lombok.Getter;

@Getter
public class PostgresDatabaseInterfaces extends DatabaseInterfacesBase {
    private final DatabaseMessageParserInterface messageParserInterface = new PostgresMessageParserInterface();
    private final DatabaseCompatibilityInterface compatibilityInterface = new PostgresCompatibilityInterface();
    private final DatabaseEnvironmentInterface environmentInterface = new DatabaseEnvironmentInterfaceImpl();
    private final DatabaseMetadataInterface metadataInterface = new PostgresMetadataInterface(this);
    private final DatabaseDataDefinitionInterface dataDefinitionInterface = new PostgresDataDefinitionInterface(this);
    private final DatabaseExecutionInterface executionInterface = new PostgresExecutionInterface();
    private final DatabaseNativeDataTypes nativeDataTypes = new PostgresNativeDataTypes();

    public PostgresDatabaseInterfaces() {
        super(
            SQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.POSTGRES_SQL),
            PSQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.POSTGRES_PSQL));
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.POSTGRES;
    }
}
