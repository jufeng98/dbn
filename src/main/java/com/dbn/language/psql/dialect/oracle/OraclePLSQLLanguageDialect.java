package com.dbn.language.psql.dialect.oracle;

import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dbn.language.common.element.TokenPairTemplate;
import com.dbn.language.psql.dialect.PSQLLanguageDialect;

public class OraclePLSQLLanguageDialect extends PSQLLanguageDialect {
    private static final TokenPairTemplate[] TOKEN_PAIR_TEMPLATES = new TokenPairTemplate[] {
            TokenPairTemplate.PARENTHESES,
            TokenPairTemplate.BEGIN_END};


    public OraclePLSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.ORACLE_PLSQL);
    }

    @Override
    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new OraclePLSQLHighlighter(this);
}

    @Override
    protected OraclePLSQLParserDefinition createParserDefinition() {
        OraclePLSQLParser parser = new OraclePLSQLParser(this);
        return new OraclePLSQLParserDefinition(parser);
    }

    @Override
    public TokenPairTemplate[] getTokenPairTemplates() {
        return TOKEN_PAIR_TEMPLATES;
    }
}
