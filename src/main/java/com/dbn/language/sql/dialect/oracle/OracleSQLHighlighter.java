package com.dbn.language.sql.dialect.oracle;

import com.dbn.language.sql.SQLSyntaxHighlighter;
import com.dbn.language.sql.dialect.SQLLanguageDialect;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LayeredLexer;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;

public class OracleSQLHighlighter extends SQLSyntaxHighlighter {
    OracleSQLHighlighter(SQLLanguageDialect languageDialect) {
        super(languageDialect, "oracle_sql_highlighter_tokens.xml");
    }

    @Override
    @NotNull
    protected Lexer createLexer() {
        FlexLexer flexLexer = new OracleSQLHighlighterFlexLexer(getTokenTypes());
        return new LayeredLexer(new FlexAdapter(flexLexer));
    }
}
