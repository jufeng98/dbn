package com.dci.intellij.dbn.language.sql;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Files;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.language.common.DBLanguageFileType;

import javax.swing.*;

public class SQLFileType extends DBLanguageFileType {
    public static final SQLFileType INSTANCE = new SQLFileType();

    public SQLFileType() {
        super(
            SQLLanguage.INSTANCE,
            Files.SQL_FILE_EXTENSIONS,
            "SQL file (DBN)",
            DBContentType.CODE);
    }

    @Override
    public Icon getIcon() {
        return Icons.FILE_SQL;
    }


}
