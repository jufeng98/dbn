package com.dbn.language.common;

import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.editor.code.options.CodeEditorGeneralSettings;
import com.dbn.editor.code.options.CodeEditorSettings;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

public class DBLSpellcheckingStrategy extends SpellcheckingStrategy implements SpellcheckingSettingsListener {


    @NotNull
    @Override
    public Tokenizer getTokenizer(PsiElement element) {
        if (element instanceof PsiWhiteSpace) {
            return EMPTY_TOKENIZER;
        }

        CodeEditorSettings codeEditorSettings = CodeEditorSettings.getInstance(element.getProject());
        CodeEditorGeneralSettings codeEditorGeneralSettings = codeEditorSettings.getGeneralSettings();
        if (codeEditorGeneralSettings.isEnableSpellchecking()) {
            if (element instanceof PsiComment) {
                return TEXT_TOKENIZER;
            }

            if (element instanceof LeafPsiElement) {
                LeafPsiElement leafPsiElement = (LeafPsiElement) element;
                PsiElement parent = leafPsiElement.getParent();
                if (parent instanceof IdentifierPsiElement) {
                    IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) parent;
                    if (identifierPsiElement.isDefinition() || codeEditorGeneralSettings.isEnableReferenceSpellchecking()) {
                        return TEXT_TOKENIZER;
                    }
                }
            }
        }

        return EMPTY_TOKENIZER;
    }

    @Override
    public void settingsChanged() {

    }
}
