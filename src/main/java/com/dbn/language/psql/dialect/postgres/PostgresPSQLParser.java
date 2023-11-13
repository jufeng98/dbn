package com.dbn.language.psql.dialect.postgres;

import com.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dbn.language.psql.PSQLParser;

class PostgresPSQLParser extends PSQLParser {
    PostgresPSQLParser(PSQLLanguageDialect languageDialect) {
        super(languageDialect, "postgres_psql_parser_tokens.xml", "postgres_psql_parser_elements.xml", "psql_block");
    }
}