package com.dbn.language.common;

import com.dbn.common.util.Files;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.mapping.FileConnectionContextManager;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.dispose.Checks.isNotValid;

public class DBSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile file) {
        if (isNotValid(project) || isNotValid(file) || !Files.isDbLanguageFile(file)) return getDefault(project, file);

        DBLanguageFileType fileType = (DBLanguageFileType) file.getFileType();
        DBLanguage<?> language = (DBLanguage<?>) fileType.getLanguage();

        ConnectionHandler connection = FileConnectionContextManager.getInstance(project).getConnection(file);
        DBLanguageDialect languageDialect = connection == null ?
                language.getMainLanguageDialect() :
                connection.getLanguageDialect(language);

        return languageDialect.getSyntaxHighlighter();
    }

    private static SyntaxHighlighter getDefault(Project project, VirtualFile virtualFile) {
        return PlainSyntaxHighlighterFactory.getSyntaxHighlighter(Language.ANY, project, virtualFile);
    }
}
