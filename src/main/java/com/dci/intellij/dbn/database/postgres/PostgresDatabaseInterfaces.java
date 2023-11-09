package com.dci.intellij.dbn.database.postgres;

import com.dci.intellij.dbn.connection.DatabaseType;
import com.dci.intellij.dbn.database.common.DatabaseEnvironmentInterfaceImpl;
import com.dci.intellij.dbn.database.common.DatabaseInterfacesBase;
import com.dci.intellij.dbn.database.common.DatabaseNativeDataTypes;
import com.dci.intellij.dbn.database.interfaces.*;
import com.dci.intellij.dbn.language.common.DBLanguageDialectIdentifier;
import com.dci.intellij.dbn.language.psql.PSQLLanguage;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
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
