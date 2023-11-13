package com.dbn.language.sql.dialect.sqlite;

import com.dbn.language.common.DBLanguageDialect;
import com.dbn.language.sql.SQLSyntaxHighlighter;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LayeredLexer;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;

public class SqliteSQLHighlighter extends SQLSyntaxHighlighter {

    public SqliteSQLHighlighter(DBLanguageDialect languageDialect) {
        super(languageDialect, "sqlite_sql_highlighter_tokens.xml");
    }

    @Override
    @NotNull
    protected Lexer createLexer() {
        FlexLexer flexLexer = new SqliteSQLHighlighterFlexLexer(getTokenTypes());
        return new LayeredLexer(new FlexAdapter(flexLexer));
    }
}