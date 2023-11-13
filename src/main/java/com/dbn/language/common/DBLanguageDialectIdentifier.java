package com.dbn.language.common;

import lombok.Getter;

@Getter
public enum DBLanguageDialectIdentifier {
    ORACLE_SQL("ORACLE-SQL"),
    ORACLE_PLSQL("ORACLE-PLSQL"),
    MYSQL_SQL("MYSQL-SQL"),
    MYSQL_PSQL("MYSQL-PSQL"),
    POSTGRES_SQL("POSTGRES-SQL"),
    POSTGRES_PSQL("POSTGRES-PSQL"),
    SQLITE_SQL("SQLITE-SQL"),
    SQLITE_PSQL("SQLITE-PSQL"),
    ISO92_SQL("ISO92-SQL");

    private final String value;

    DBLanguageDialectIdentifier(String value) {
        this.value = value;
    }
}
