package com.dbn.language.psql;

import com.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dbn.code.psql.color.PSQLTextAttributesKeys;
import com.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dbn.language.common.SharedTokenTypeBundle;
import com.dbn.language.common.TokenTypeBundle;

public abstract class PSQLSyntaxHighlighter extends DBLanguageSyntaxHighlighter {
    public PSQLSyntaxHighlighter(PSQLLanguageDialect languageDialect, String tokenTypesFile) {
        super(languageDialect, tokenTypesFile);
        TokenTypeBundle tt = getTokenTypes();
        SharedTokenTypeBundle stt = tt.getSharedTokenTypes();
        colors.put(stt.getIdentifier(),                  PSQLTextAttributesKeys.IDENTIFIER);
        colors.put(stt.getQuotedIdentifier(),            PSQLTextAttributesKeys.QUOTED_IDENTIFIER);
        colors.put(tt.getTokenType("LINE_COMMENT"),      PSQLTextAttributesKeys.LINE_COMMENT);
        colors.put(tt.getTokenType("BLOCK_COMMENT"),     PSQLTextAttributesKeys.BLOCK_COMMENT);
        colors.put(tt.getTokenType("STRING"),            PSQLTextAttributesKeys.STRING);
        colors.put(tt.getTokenSet("NUMBERS"),            PSQLTextAttributesKeys.NUMBER);
        colors.put(tt.getTokenSet("KEYWORDS"),           PSQLTextAttributesKeys.KEYWORD);
        colors.put(tt.getTokenSet("FUNCTIONS"),          PSQLTextAttributesKeys.FUNCTION);
        colors.put(tt.getTokenSet("PARAMETERS"),         PSQLTextAttributesKeys.PARAMETER);
        colors.put(tt.getTokenSet("DATA_TYPES"),         PSQLTextAttributesKeys.DATA_TYPE);
        colors.put(tt.getTokenSet("PARENTHESES"),        PSQLTextAttributesKeys.PARENTHESIS);
        colors.put(tt.getTokenSet("BRACKETS"),           PSQLTextAttributesKeys.BRACKET);
        colors.put(tt.getTokenSet("OPERATORS"),          PSQLTextAttributesKeys.OPERATOR);
        colors.put(tt.getTokenSet("EXCEPTIONS"),         PSQLTextAttributesKeys.EXCEPTION);

        fillMap(colors, tt.getTokenSet("KEYWORDS"),      PSQLTextAttributesKeys.KEYWORD);
        fillMap(colors, tt.getTokenSet("FUNCTIONS"),     PSQLTextAttributesKeys.FUNCTION);
        fillMap(colors, tt.getTokenSet("DATA_TYPES"),    PSQLTextAttributesKeys.DATA_TYPE);
        fillMap(colors, tt.getTokenSet("NUMBERS"),       PSQLTextAttributesKeys.NUMBER);

        fillMap(colors, tt.getTokenSet("OPERATORS"),     PSQLTextAttributesKeys.OPERATOR);
        fillMap(colors, tt.getTokenSet("BRACKETS"),      PSQLTextAttributesKeys.BRACKET);
        fillMap(colors, tt.getTokenSet("PARENTHESES"),   PSQLTextAttributesKeys.PARENTHESIS);
    }
}