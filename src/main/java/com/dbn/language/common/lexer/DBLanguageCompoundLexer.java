package com.dbn.language.common.lexer;

public interface DBLanguageCompoundLexer extends DBLanguageLexer {
    String getCurrentToken();
    int getCurrentPosition();

    void setTokenStart(int tokenStart);
    void yypushback(int number);
    int yylength();
}
