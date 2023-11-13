package com.dbn.language.sql.dialect.sqlite;

import com.dbn.language.common.DBLanguageDialect;
import com.dbn.language.sql.SQLParser;

public class SqliteSQLParser extends SQLParser {
    public SqliteSQLParser(DBLanguageDialect languageDialect, String parseRootId) {
        super(languageDialect, "sqlite_sql_parser_tokens.xml", "sqlite_sql_parser_elements.xml", parseRootId);
    }

    SqliteSQLParser(DBLanguageDialect languageDialect) {
        this(languageDialect, "sql_block");
    }
}