package com.dbn.database.oracle;

import com.dbn.connection.DatabaseType;
import com.dbn.database.common.DatabaseInterfacesBase;
import com.dbn.database.common.DatabaseNativeDataTypes;
import com.dbn.database.interfaces.*;
import com.dbn.database.interfaces.*;
import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.psql.PSQLLanguage;
import com.dbn.language.sql.SQLLanguage;
import lombok.Getter;

@Getter
public class OracleDatabaseInterfaces extends DatabaseInterfacesBase {
    private final DatabaseMessageParserInterface messageParserInterface = new OracleMessageParserInterface();
    private final DatabaseCompatibilityInterface compatibilityInterface = new OracleCompatibilityInterface();
    private final DatabaseEnvironmentInterface environmentInterface = new OracleEnvironmentInterface();
    private final DatabaseMetadataInterface metadataInterface = new OracleMetadataInterface(this);
    private final DatabaseDebuggerInterface debuggerInterface = new OracleDebuggerInterface(this);
    private final DatabaseDataDefinitionInterface dataDefinitionInterface = new OracleDataDefinitionInterface(this);
    private final DatabaseExecutionInterface executionInterface = new OracleExecutionInterface();
    private final DatabaseNativeDataTypes nativeDataTypes = new OracleNativeDataTypes();


    public OracleDatabaseInterfaces() {
        super(SQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.ORACLE_SQL),
                PSQLLanguage.INSTANCE.getLanguageDialect(DBLanguageDialectIdentifier.ORACLE_PLSQL));
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.ORACLE;
    }
}