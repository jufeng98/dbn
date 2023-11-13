package com.dbn.language.psql;

import com.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dbn.language.psql.dialect.mysql.MysqlPSQLLanguageDialect;
import com.dbn.language.psql.dialect.oracle.OraclePLSQLLanguageDialect;
import com.dbn.language.psql.dialect.postgres.PostgresPSQLLanguageDialect;
import com.dbn.language.psql.dialect.sqlite.SqlitePSQLLanguageDialect;
import com.dbn.code.psql.style.PSQLCodeStyle;
import com.dbn.code.psql.style.options.PSQLCodeStyleSettings;
import com.dbn.language.common.DBLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.Nullable;

public class PSQLLanguage extends DBLanguage<PSQLLanguageDialect> {
    public static final String ID = "DBN-PSQL";
    public static final PSQLLanguage INSTANCE = new PSQLLanguage();

    @Override
    protected PSQLLanguageDialect[] createLanguageDialects() {
        PSQLLanguageDialect oraclePLSQLLanguageDialect = new OraclePLSQLLanguageDialect();
        PSQLLanguageDialect mysqlPSQLLanguageDialect = new MysqlPSQLLanguageDialect();
        PSQLLanguageDialect postgresPSQLLanguageDialect = new PostgresPSQLLanguageDialect();
        PSQLLanguageDialect sqlitePSQLLanguageDialect = new SqlitePSQLLanguageDialect();
        return new PSQLLanguageDialect[]{
                oraclePLSQLLanguageDialect,
                mysqlPSQLLanguageDialect,
                postgresPSQLLanguageDialect,
                sqlitePSQLLanguageDialect};
    }

    @Override
    public PSQLLanguageDialect getMainLanguageDialect() {
        return getAvailableLanguageDialects()[0];
    }

    @Override
    protected IFileElementType createFileElementType() {
        return new PSQLFileElementType(this);
    }

    private PSQLLanguage() {
        super(ID, "text/plsql");
    }

    @Override
    public PSQLCodeStyleSettings codeStyleSettings(@Nullable Project project) {
        return PSQLCodeStyle.settings(project);
    }
}