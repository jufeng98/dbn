package com.dbn.sql.parser;

import com.dbn.sql.SqlLanguage;
import com.dbn.sql.psi.SqlTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * @author yudong
 */
public class SqlParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(SqlLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new com.dbn.sql.parser.SqlAdapter();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new SqlParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return com.dbn.sql.parser.SqlTokenSets.SQL_COMMENT;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return com.dbn.sql.parser.SqlTokenSets.STRING_LITERALS;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return SqlTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new com.dbn.sql.parser.SqlFile(viewProvider);
    }
}
