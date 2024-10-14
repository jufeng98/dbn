package com.dbn.sql;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author yudong
 */
public class SqlFileType extends LanguageFileType {

    public static final SqlFileType INSTANCE = new SqlFileType();

    private SqlFileType() {
        super(SqlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "SQL";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "SQL language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "mysql";
    }

    @Override
    public Icon getIcon() {
        return SqlIcons.FILE;
    }

}
