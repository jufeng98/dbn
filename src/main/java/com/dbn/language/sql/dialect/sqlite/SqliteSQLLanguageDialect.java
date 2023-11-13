package com.dbn.language.sql.dialect.sqlite;

import com.dbn.language.common.ChameleonTokenType;
import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dbn.language.sql.dialect.SQLLanguageDialect;

import java.util.Set;

public class SqliteSQLLanguageDialect extends SQLLanguageDialect {

    public SqliteSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.SQLITE_SQL);
    }

    @Override
    protected Set<ChameleonTokenType> createChameleonTokenTypes() {
        return null;
    }

    @Override
    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new SqliteSQLHighlighter(this);
}

    @Override
    protected SqliteSQLParserDefinition createParserDefinition() {
        SqliteSQLParser parser = new SqliteSQLParser(this);
        return new SqliteSQLParserDefinition(parser);
    }

}