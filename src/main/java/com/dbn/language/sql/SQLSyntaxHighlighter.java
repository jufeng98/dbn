package com.dbn.language.sql;

import com.dbn.code.sql.color.SQLTextAttributesKeys;
import com.dbn.language.common.DBLanguageDialect;
import com.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dbn.language.common.SharedTokenTypeBundle;
import com.dbn.language.common.TokenTypeBundle;

public abstract class SQLSyntaxHighlighter extends DBLanguageSyntaxHighlighter {
    @SuppressWarnings("unchecked")
    public SQLSyntaxHighlighter(DBLanguageDialect languageDialect, String tokenTypesFile) {
        super(languageDialect, tokenTypesFile);
        TokenTypeBundle tt = getTokenTypes();
        SharedTokenTypeBundle stt = tt.getSharedTokenTypes();
        colors.put(stt.getIdentifier(),                  SQLTextAttributesKeys.IDENTIFIER);
        colors.put(stt.getQuotedIdentifier(),            SQLTextAttributesKeys.QUOTED_IDENTIFIER);
        colors.put(tt.getTokenType("LINE_COMMENT"),      SQLTextAttributesKeys.LINE_COMMENT);
        colors.put(tt.getTokenType("BLOCK_COMMENT"),     SQLTextAttributesKeys.BLOCK_COMMENT);
        colors.put(tt.getTokenType("STRING"),            SQLTextAttributesKeys.STRING);
        colors.put(tt.getTokenSet("NUMBERS"),            SQLTextAttributesKeys.NUMBER);
        colors.put(tt.getTokenSet("KEYWORDS"),           SQLTextAttributesKeys.KEYWORD);
        colors.put(tt.getTokenSet("FUNCTIONS"),          SQLTextAttributesKeys.FUNCTION);
        colors.put(tt.getTokenSet("PARAMETERS"),         SQLTextAttributesKeys.PARAMETER);
        colors.put(tt.getTokenSet("DATA_TYPES"),         SQLTextAttributesKeys.DATA_TYPE);
        colors.put(tt.getTokenSet("VARIABLES"),          SQLTextAttributesKeys.VARIABLE);
        colors.put(tt.getTokenSet("PARENTHESES"),        SQLTextAttributesKeys.PARENTHESIS);
        colors.put(tt.getTokenSet("BRACKETS"),           SQLTextAttributesKeys.BRACKET);
        colors.put(tt.getTokenSet("OPERATORS"),          SQLTextAttributesKeys.OPERATOR);

        fillMap(colors, tt.getTokenSet("NUMBERS"),       SQLTextAttributesKeys.NUMBER);
        fillMap(colors, tt.getTokenSet("KEYWORDS"),      SQLTextAttributesKeys.KEYWORD);
        fillMap(colors, tt.getTokenSet("FUNCTIONS"),     SQLTextAttributesKeys.FUNCTION);
        fillMap(colors, tt.getTokenSet("PARAMETERS"),    SQLTextAttributesKeys.PARAMETER);
        fillMap(colors, tt.getTokenSet("DATA_TYPES"),    SQLTextAttributesKeys.DATA_TYPE);
        fillMap(colors, tt.getTokenSet("VARIABLES"),     SQLTextAttributesKeys.VARIABLE);

        fillMap(colors, tt.getTokenSet("PARENTHESES"),   SQLTextAttributesKeys.PARENTHESIS);
        fillMap(colors, tt.getTokenSet("BRACKETS"),      SQLTextAttributesKeys.BRACKET);
        fillMap(colors, tt.getTokenSet("OPERATORS"),     SQLTextAttributesKeys.OPERATOR);
    }
}
