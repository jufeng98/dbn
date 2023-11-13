package com.dbn.language.psql.dialect.sqlite;

import com.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dbn.language.sql.dialect.sqlite.SqliteSQLParser;

public class SqlitePSQLParser extends SqliteSQLParser {
    SqlitePSQLParser(PSQLLanguageDialect languageDialect) {
        super(languageDialect, "psql_block");
    }

    @Override
    protected Class getResourceLookupClass() {
        return SqliteSQLParser.class;
    }
}
