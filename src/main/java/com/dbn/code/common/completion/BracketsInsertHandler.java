package com.dbn.code.common.completion;

import com.dbn.code.common.lookup.CodeCompletionLookupItem;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;

public class BracketsInsertHandler extends BasicInsertHandler{
    public static final BracketsInsertHandler INSTANCE = new BracketsInsertHandler();

    @Override
    public void handleInsert(InsertionContext insertionContext, CodeCompletionLookupItem lookupElement) {
        Editor editor = insertionContext.getEditor();
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        int startOffset = insertionContext.getStartOffset();
        char completionChar = insertionContext.getCompletionChar();

        int endOffset = startOffset + lookupElement.getLookupString().length();
        document.insertString(endOffset, "()");

        if (completionChar == ' ') {
            caretModel.moveCaretRelatively(3, 0, false, false, false);
        } else {
            caretModel.moveCaretRelatively(1, 0, false, false, false);
        }
    }
}