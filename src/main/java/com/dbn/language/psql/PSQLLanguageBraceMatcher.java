package com.dbn.language.psql;

import com.dbn.language.common.DBLanguageBraceMatcher;

public class PSQLLanguageBraceMatcher extends DBLanguageBraceMatcher {
    public PSQLLanguageBraceMatcher() {
        super(PSQLLanguage.INSTANCE);
    }
}