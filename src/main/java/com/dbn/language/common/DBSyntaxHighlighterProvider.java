package com.dbn.language.common;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.dispose.Checks.isNotValid;

public class DBSyntaxHighlighterProvider implements SyntaxHighlighterProvider {

    @Override
    @Nullable
    public SyntaxHighlighter create(@NotNull FileType fileType, @Nullable Project project, @Nullable VirtualFile file) {
        if (isNotValid(project)) return null;
        if (!(fileType instanceof DBLanguageFileType)) return null;

        DBLanguageFileType dbFileType = (DBLanguageFileType) fileType;
        DBLanguage language = (DBLanguage) dbFileType.getLanguage();

        DBLanguageDialect languageDialect = DBLanguageDialect.get(language, file, project);
        if (languageDialect == null) languageDialect = language.getMainLanguageDialect();

        return languageDialect.getSyntaxHighlighter();
    }
}
