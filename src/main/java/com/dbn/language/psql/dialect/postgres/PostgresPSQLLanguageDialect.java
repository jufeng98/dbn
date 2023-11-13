package com.dbn.language.psql.dialect.postgres;

import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dbn.language.psql.dialect.PSQLLanguageDialect;

public class PostgresPSQLLanguageDialect extends PSQLLanguageDialect {
    public PostgresPSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.POSTGRES_PSQL);
    }

    @Override
    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new PostgresPSQLHighlighter(this);
}

    @Override
    protected PostgresPSQLParserDefinition createParserDefinition() {
        PostgresPSQLParser parser = new PostgresPSQLParser(this);
        return new PostgresPSQLParserDefinition(parser);
    }

}
