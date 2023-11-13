package com.dbn.language.psql.dialect.sqlite;

import com.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dbn.language.sql.dialect.sqlite.SqliteSQLHighlighter;

public class SqlitePSQLHighlighter extends SqliteSQLHighlighter {
    SqlitePSQLHighlighter(PSQLLanguageDialect languageDialect) {
        super(languageDialect);
    }

    @Override
    protected Class getResourceLookupClass() {
        return SqliteSQLHighlighter.class;
    }
}
