package com.dbn.language.common.lexer;

import com.dbn.language.common.SharedTokenTypeBundle;
import com.dbn.language.common.TokenTypeBundle;

public abstract class DBLanguageLexerBase implements DBLanguageLexer{
    protected final TokenTypeBundle tt;
    protected final SharedTokenTypeBundle stt;

    public DBLanguageLexerBase() {
        throw new UnsupportedOperationException();
    }

    protected DBLanguageLexerBase(TokenTypeBundle tt) {
        this.tt = tt;
        this.stt = tt.getSharedTokenTypes();
    }
}
