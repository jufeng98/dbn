package com.dbn.language.sql;

import com.dbn.language.common.DBLanguageDialect;
import com.dbn.language.common.DBLanguageParser;

public class SQLParser extends DBLanguageParser {
    public SQLParser(DBLanguageDialect languageDialect, String tokenTypesFile, String elementTypesFile, String defaultParseRootId) {
        super(languageDialect, tokenTypesFile, elementTypesFile, defaultParseRootId);
    }
}
