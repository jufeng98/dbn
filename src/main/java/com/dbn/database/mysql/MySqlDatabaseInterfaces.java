package com.dbn.database.mysql;

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
