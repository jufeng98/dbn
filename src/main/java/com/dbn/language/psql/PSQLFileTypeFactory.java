package com.dbn.language.psql;

import com.dbn.language.common.DBLanguageFileTypeFactory;
import com.intellij.openapi.fileTypes.FileType;

@Deprecated
public class PSQLFileTypeFactory extends DBLanguageFileTypeFactory {

    @Override
    protected FileType getFileType() {
        return PSQLFileType.INSTANCE;
    }
}