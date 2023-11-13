package com.dbn.language.psql;

import com.dbn.common.icon.Icons;
import com.dbn.common.util.Files;
import com.dbn.editor.DBContentType;
import com.dbn.language.common.DBLanguageFileType;

import javax.swing.*;

public class PSQLFileType extends DBLanguageFileType {
    public static final PSQLFileType INSTANCE = new PSQLFileType();

    private PSQLFileType() {
        super(PSQLLanguage.INSTANCE,
                Files.PSQL_FILE_EXTENSIONS,
                "PSQL file (DBN)",
                DBContentType.CODE);
    }

    @Override
    public Icon getIcon() {
        return Icons.FILE_PLSQL;
    }


}