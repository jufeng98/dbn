package com.dbn.language.common;

import com.dbn.common.thread.Read;
import com.dbn.connection.ConnectionHandler;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.NotNull;

public class DBLanguageFileElementType extends IFileElementType {
    public DBLanguageFileElementType(Language language) {
        super("FILE_ELEMENT_TYPE", language, false);
    }

    @Override
    public ASTNode parseContents(ASTNode chameleon) {
        DBLanguagePsiFile file = (DBLanguagePsiFile) chameleon.getPsi();
        Project project = file.getProject();

        DBLanguageDialect languageDialect = file.getLanguageDialect();
        if (languageDialect == null) return super.parseContents(chameleon);

        /*DBLanguageFile originalFile = (DBLanguageFile) file.getViewProvider().getAllFiles().get(0).getOriginalFile();
        if (originalFile != null)  file = originalFile;*/

        String text = chameleon.getText();
        DBLanguageParserDefinition parserDefinition = languageDialect.getParserDefinition();
        Lexer lexer = parserDefinition.createLexer(project);

        DBLanguageParser parser = parserDefinition.createParser(project);
        double databaseVersion = 9999;
        ConnectionHandler connection = file.getConnection();
        if (connection != null) {
            databaseVersion = connection.getDatabaseVersion();
        }

        PsiBuilder builder = Read.call(() -> createBuilder(chameleon, project, lexer, languageDialect, text));
        ASTNode node = parser.parse(this, builder, file.getParseRootId(), databaseVersion);
        return node.getFirstChildNode();
    }

    @NotNull
    private static PsiBuilder createBuilder(ASTNode chameleon, Project project, Lexer lexer, DBLanguageDialect languageDialect, String text) {
        PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
        return factory.createBuilder(project, chameleon, lexer, languageDialect, text);
    }
}
