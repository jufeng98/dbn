package com.dbn.language.sql.dialect.iso92;

import com.dbn.language.common.ChameleonTokenType;
import com.dbn.language.common.DBLanguageDialectIdentifier;
import com.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dbn.language.sql.dialect.SQLLanguageDialect;

import java.util.Set;

public class Iso92SQLLanguageDialect extends SQLLanguageDialect {

    public Iso92SQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.ISO92_SQL);
    }

    @Override
    protected Set<ChameleonTokenType> createChameleonTokenTypes() {
        return null;
    }

    @Override
    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new Iso92SQLHighlighter(this);
}

    @Override
    protected Iso92SQLParserDefinition createParserDefinition() {
        Iso92SQLParser parser = new Iso92SQLParser(this);
        return new Iso92SQLParserDefinition(parser);
    }

}
