package com.dbn.code.common.style.formatting;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FormattingDocumentModel implements com.intellij.formatting.FormattingDocumentModel {
    @Override
    public int getLineNumber(int offset) {
        return 0;
    }

    @Override
    public int getLineStartOffset(int line) {
        return 0;
    }

    @Override
    public CharSequence getText(final TextRange textRange) {
        return null;
    }

    @Override
    public int getTextLength() {
        return 0;
    }

    @Override
    @NotNull
    public Document getDocument() {
        return null;
    }

    @Override
    public boolean containsWhiteSpaceSymbolsOnly(int startOffset, int endOffset) {
        return false;
    }

    @Override
    @NotNull
    public CharSequence adjustWhiteSpaceIfNecessary(@NotNull CharSequence whiteSpaceText, int startOffset, int endOffset, @Nullable ASTNode nodeAfter, boolean changedViaPsi) {
        return whiteSpaceText;
    }

    @NotNull
    public CharSequence adjustWhiteSpaceIfNecessary(@NotNull CharSequence charSequence, int i, int i1, boolean b) {
        return charSequence;
    }

    @NotNull
    public CharSequence adjustWhiteSpaceIfNecessary(@NotNull CharSequence whiteSpaceText, int startOffset, int endOffset) {
        return whiteSpaceText;
    }
}
