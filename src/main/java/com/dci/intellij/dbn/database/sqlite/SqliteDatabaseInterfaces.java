package com.dci.intellij.dbn.database.sqlite;

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
public class SqliteDatabaseInterfaces extends DatabaseInterfacesBase {
    private final DatabaseMessageParserInterface messageParserInterface = new SqliteMessageParserInterface();
    private final DatabaseCompatibilityInterface compatibilityInterface = new SqliteCompatibilityInterface();
    private final DatabaseEnvironmentInterface environmentInterface = new DatabaseEnvironmentInterfaceImpl();
    private final DatabaseMetadataInterface metadataInterface = new SqliteMetadataInterface(this);
    private final DatabaseDataDefinitionInterface dataDefinitionInterface = new SqliteDataDefinitionInterface(this);
    private final DatabaseExecutionInterface executionInterface = new SqliteExecutionInterface();
    private final DatabaseNativeDataTypes nativeDataTypes = new SqliteNativeDataTypes();

    public SqliteDatabaseInterfaces() {
        super(SQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.SQLITE_SQL),
                PSQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.SQLITE_PSQL));
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.SQLITE;
    }
}
