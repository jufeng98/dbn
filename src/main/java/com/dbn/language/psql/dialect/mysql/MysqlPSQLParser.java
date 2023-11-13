package com.dbn.language.psql.dialect.mysql;

import com.dbn.language.psql.PSQLParser;
import com.dbn.language.psql.dialect.PSQLLanguageDialect;

public class MysqlPSQLParser extends PSQLParser {
    public MysqlPSQLParser(PSQLLanguageDialect languageDialect) {
        super(languageDialect, "mysql_psql_parser_tokens.xml", "mysql_psql_parser_elements.xml", "psql_block");
    }
}