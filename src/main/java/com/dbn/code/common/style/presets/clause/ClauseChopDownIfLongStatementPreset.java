package com.dbn.code.common.style.presets.clause;

import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.NamedPsiElement;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class ClauseChopDownIfLongStatementPreset extends ClauseAbstractPreset {
    public ClauseChopDownIfLongStatementPreset() {
        super("chop_down_if_statement_long", "Chop down if statement long");
    }

    @Override
    @Nullable
    public Wrap getWrap(BasePsiElement psiElement, CodeStyleSettings settings) {
        NamedPsiElement namedPsiElement = getEnclosingStatementElement(psiElement);
        boolean shouldWrap = namedPsiElement != null && namedPsiElement.approximateLength() > settings.getRightMargin(psiElement.getLanguage());
        return shouldWrap ? WRAP_ALWAYS : WRAP_NONE;

    }

    @Override
    @Nullable
    public Spacing getSpacing(BasePsiElement psiElement, CodeStyleSettings settings) {
        NamedPsiElement namedPsiElement = getEnclosingStatementElement(psiElement);
        boolean shouldWrap = namedPsiElement!= null && namedPsiElement.approximateLength() > settings.getRightMargin(psiElement.getLanguage());
        return getSpacing(psiElement, shouldWrap);
    }

    @Nullable
    private NamedPsiElement getEnclosingStatementElement(BasePsiElement psiElement) {
        BasePsiElement<?> parentPsiElement = getParentPsiElement(psiElement);
        if (parentPsiElement != null) {
            DBLanguagePsiFile psiFile = parentPsiElement.getFile();
            NamedPsiElement namedPsiElement = parentPsiElement.findEnclosingElement(ElementTypeAttribute.STATEMENT);
            if (namedPsiElement == null) {
                PsiElement childPsiElement = psiFile.getFirstChild();
                while (childPsiElement != null) {
                    if (childPsiElement instanceof NamedPsiElement) {
                        return (NamedPsiElement) childPsiElement;
                    }
                    childPsiElement = childPsiElement.getNextSibling();
                }
            } else {
                return namedPsiElement;
            }
        }
        return null;
    }

}