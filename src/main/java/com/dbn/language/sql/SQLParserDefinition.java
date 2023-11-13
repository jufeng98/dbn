package com.dbn.language.sql;

import com.dbn.language.common.DBLanguageParser;
import com.dbn.language.common.DBLanguageParserDefinition;
import com.dbn.language.common.TokenTypeBundle;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;


public class SQLParserDefinition extends DBLanguageParserDefinition {

    public SQLParserDefinition() {
        this((SQLParser) getDefaultParseDefinition().getParser());
    }

    public SQLParserDefinition(SQLParser parser) {
        super(parser);
    }

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return getDefaultParseDefinition().createLexer(project);
    }

    @NotNull
    private static DBLanguageParserDefinition getDefaultParseDefinition() {
        return SQLLanguage.INSTANCE.getMainLanguageDialect().getParserDefinition();
    }

    @Override
    @NotNull
    public DBLanguageParser createParser(Project project) {
        return getParser();
    }

    public TokenTypeBundle getTokenTypes() {
        return getParser().getTokenTypes();
    }

    @NotNull
    @Override
    public PsiFile createPsiFile(FileViewProvider viewProvider) {
        return new SQLFile(viewProvider);
    }
}
