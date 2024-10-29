package com.dbn.vfs.file;

import com.dbn.language.common.DBLanguage;
import com.dbn.object.DBConsole;
import com.dbn.sql.SqlFileType;
import com.dbn.sql.parser.SqlFile;
import com.dbn.vfs.DatabaseFileViewProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author yudong
 */
public class MySqlDBConsoleVirtualFile extends DBConsoleVirtualFile {

    public MySqlDBConsoleVirtualFile(@NotNull DBConsole console) {
        super(console);
    }

    @Override
    public PsiFile initializePsiFile(DatabaseFileViewProvider fileViewProvider, DBLanguage<?> language) {
        return new SqlFile(fileViewProvider);
    }

    @Override
    public String getExtension() {
        return "mysql";
    }

    @Override
    public @NotNull FileType getFileType() {
        return SqlFileType.INSTANCE;
    }
}
