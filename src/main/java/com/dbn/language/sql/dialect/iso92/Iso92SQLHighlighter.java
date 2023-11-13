package com.dbn.language.sql.dialect.iso92;

import com.dbn.language.sql.SQLSyntaxHighlighter;
import com.dbn.language.sql.dialect.SQLLanguageDialect;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LayeredLexer;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;

public class Iso92SQLHighlighter extends SQLSyntaxHighlighter {
    Iso92SQLHighlighter(SQLLanguageDialect languageDialect) {
        super(languageDialect, "iso92_sql_highlighter_tokens.xml");
    }

    @Override
    @NotNull
    protected Lexer createLexer() {
        FlexLexer flexLexer = new Iso92SQLHighlighterFlexLexer(getTokenTypes());
        return new LayeredLexer(new FlexAdapter(flexLexer));
    }
}
