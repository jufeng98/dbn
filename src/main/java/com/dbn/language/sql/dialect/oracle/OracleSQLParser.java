package com.dbn.language.sql.dialect.oracle;

import com.dbn.language.sql.SQLParser;
import com.dbn.language.sql.dialect.SQLLanguageDialect;

class OracleSQLParser extends SQLParser {
    OracleSQLParser(SQLLanguageDialect languageDialect) {
        super(languageDialect, "oracle_sql_parser_tokens.xml", "oracle_sql_parser_elements.xml", "sql_block");
    }
}
