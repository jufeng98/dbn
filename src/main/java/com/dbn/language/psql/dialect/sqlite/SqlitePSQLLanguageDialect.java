package com.dbn.language.psql.dialect.sqlite;

import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dbn.language.sql.dialect.sqlite.SqliteSQLParserDefinition;

public class SqlitePSQLLanguageDialect extends PSQLLanguageDialect {

    public SqlitePSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.SQLITE_PSQL);
    }

    @Override
    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new SqlitePSQLHighlighter(this);
    }

    @Override
    protected SqliteSQLParserDefinition createParserDefinition() {
        SqlitePSQLParser parser = new SqlitePSQLParser(this);
        return new SqliteSQLParserDefinition(parser);
    }
}