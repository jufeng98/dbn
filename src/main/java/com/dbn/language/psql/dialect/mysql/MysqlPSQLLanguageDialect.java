package com.dbn.language.psql.dialect.mysql;

import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dbn.language.psql.dialect.oracle.OraclePLSQLHighlighter;
import com.dbn.language.psql.dialect.PSQLLanguageDialect;

public class MysqlPSQLLanguageDialect extends PSQLLanguageDialect {
    public MysqlPSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.MYSQL_PSQL);
    }

    @Override
    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new OraclePLSQLHighlighter(this);
}

    @Override
    protected MysqlPSQLParserDefinition createParserDefinition() {
        MysqlPSQLParser parser = new MysqlPSQLParser(this);
        return new MysqlPSQLParserDefinition(parser);
    }
}
