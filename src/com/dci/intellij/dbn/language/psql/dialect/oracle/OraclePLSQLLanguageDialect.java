package com.dci.intellij.dbn.language.psql.dialect.oracle;

import com.dci.intellij.dbn.language.common.DBLanguageDialectIdentifier;
import com.dci.intellij.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dci.intellij.dbn.language.common.element.TokenPairTemplate;
import com.dci.intellij.dbn.language.common.element.parser.TokenPairRangeMonitor;
import com.dci.intellij.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.intellij.lang.PsiBuilder;

import java.util.Map;

public class OraclePLSQLLanguageDialect extends PSQLLanguageDialect {
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
    public Map<TokenPairTemplate, TokenPairRangeMonitor> createTokenPairRangeMonitors(PsiBuilder builder) {
        Map<TokenPairTemplate, TokenPairRangeMonitor> tokenPairRangeMonitors = super.createTokenPairRangeMonitors(builder);
        tokenPairRangeMonitors.put(TokenPairTemplate.BEGIN_END, new TokenPairRangeMonitor(builder, this, TokenPairTemplate.BEGIN_END));
        return tokenPairRangeMonitors;
    }
}
