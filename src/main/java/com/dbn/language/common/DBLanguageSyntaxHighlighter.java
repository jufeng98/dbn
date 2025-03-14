package com.dbn.language.common;

import com.dbn.common.util.XmlContents;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jdom.Document;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class DBLanguageSyntaxHighlighter extends SyntaxHighlighterBase {
    @SuppressWarnings("rawtypes")
    protected Map colors = new HashMap<>();
    @SuppressWarnings("rawtypes")
    protected Map backgrounds = new HashMap<>();

    private final DBLanguageDialect languageDialect;
    private final TokenTypeBundle tokenTypes;

    public DBLanguageSyntaxHighlighter(DBLanguageDialect languageDialect, String tokenTypesFile) {
        Document document = loadDefinition(tokenTypesFile);
        tokenTypes = new TokenTypeBundle(languageDialect, document);
        this.languageDialect = languageDialect;
    }

    @SneakyThrows
    private Document loadDefinition(String tokenTypesFile) {
        return XmlContents.fileToDocument(getResourceLookupClass(), tokenTypesFile);
    }

    protected Class<?> getResourceLookupClass() {
        return getClass();
    }

    @NotNull
    protected abstract Lexer createLexer();

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType instanceof SimpleTokenType<?> simpleTokenType) {
            return simpleTokenType.getTokenHighlights(() -> pack(
                        getAttributeKeys(tokenType, backgrounds),
                        getAttributeKeys(tokenType, colors)));
        } else {
            return TextAttributesKey.EMPTY_ARRAY;
        }
    }

    private static TextAttributesKey getAttributeKeys(IElementType tokenType, Map<?, ?> map) {
        return (TextAttributesKey) map.get(tokenType);
    }

    @Override
    @NotNull
    public Lexer getHighlightingLexer() {
        return createLexer();
    }
}
