package com.dbn.language.sql;

import com.dbn.language.common.DBLanguageFileTypeFactory;
import com.intellij.openapi.fileTypes.FileType;

@Deprecated
public class SQLFileTypeFactory extends DBLanguageFileTypeFactory {

    @Override
    protected FileType getFileType() {
        return SQLFileType.INSTANCE;
    }
}
