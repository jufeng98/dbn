package com.dbn.language.psql.dialect.postgres;

import com.dbn.language.psql.PSQLSyntaxHighlighter;
import com.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LayeredLexer;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;

public class PostgresPSQLHighlighter extends PSQLSyntaxHighlighter {
    PostgresPSQLHighlighter(PSQLLanguageDialect languageDialect) {
        super(languageDialect, "postgres_psql_highlighter_tokens.xml");
    }

    @Override
    @NotNull
    protected Lexer createLexer() {
        FlexLexer flexLexer = new PostgresPSQLHighlighterFlexLexer(getTokenTypes());
        return new LayeredLexer(new FlexAdapter(flexLexer));
    }
}