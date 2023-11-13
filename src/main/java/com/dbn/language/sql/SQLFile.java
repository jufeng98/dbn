package com.dbn.language.sql;

import com.dbn.language.common.DBLanguagePsiFile;
import com.intellij.psi.FileViewProvider;

public class SQLFile extends DBLanguagePsiFile {
    SQLFile(FileViewProvider fileViewProvider) {
        super(fileViewProvider, SQLFileType.INSTANCE, SQLLanguage.INSTANCE);
    }
}