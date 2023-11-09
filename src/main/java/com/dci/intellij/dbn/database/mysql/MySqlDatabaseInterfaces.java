package com.dci.intellij.dbn.database.mysql;

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
public final class MySqlDatabaseInterfaces extends DatabaseInterfacesBase {
    private final DatabaseMessageParserInterface messageParserInterface = new MySqlMessageParserInterface();
    private final DatabaseCompatibilityInterface compatibilityInterface = new MySqlCompatibilityInterface();
    private final DatabaseEnvironmentInterface environmentInterface = new DatabaseEnvironmentInterfaceImpl();
    private final DatabaseMetadataInterface metadataInterface = new MySqlMetadataInterface(this);
    private final DatabaseDataDefinitionInterface dataDefinitionInterface = new MySqlDataDefinitionInterface(this);
    private final DatabaseExecutionInterface executionInterface = new MySqlExecutionInterface();
    private final DatabaseNativeDataTypes nativeDataTypes = new MySqlNativeDataTypes();

    public MySqlDatabaseInterfaces() {
        super(SQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.MYSQL_SQL),
                PSQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.MYSQL_PSQL));
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }
}
