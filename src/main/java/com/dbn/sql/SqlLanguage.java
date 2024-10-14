package com.dbn.sql;

import com.intellij.lang.Language;

/**
 * @author yudong
 */
public class SqlLanguage extends Language {

    public static final SqlLanguage INSTANCE = new SqlLanguage();

    private SqlLanguage() {
        super("sql");
    }

}
