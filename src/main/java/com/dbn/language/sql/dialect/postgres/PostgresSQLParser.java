package com.dbn.language.sql.dialect.postgres;

import com.dbn.language.sql.SQLParser;
import com.dbn.language.sql.dialect.SQLLanguageDialect;

public class PostgresSQLParser extends SQLParser {
    public PostgresSQLParser(SQLLanguageDialect languageDialect) {
        super(languageDialect, "postgres_sql_parser_tokens.xml", "postgres_sql_parser_elements.xml", "sql_block");
    }
}