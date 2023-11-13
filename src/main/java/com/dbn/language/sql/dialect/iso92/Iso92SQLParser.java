package com.dbn.language.sql.dialect.iso92;

import com.dbn.language.sql.SQLParser;
import com.dbn.language.sql.dialect.SQLLanguageDialect;

class Iso92SQLParser extends SQLParser {
    Iso92SQLParser(SQLLanguageDialect languageDialect) {
        super(languageDialect, "iso92_sql_parser_tokens.xml", "iso92_sql_parser_elements.xml", "sql_block");
    }
}
