package com.dbn.code.common.style.presets.clause;

import com.dbn.code.common.style.presets.CodeStylePresetImpl;
import com.dbn.language.common.SharedTokenTypeBundle;
import com.dbn.language.common.TokenType;
import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.TokenPsiElement;
import com.intellij.formatting.Spacing;
import com.intellij.psi.PsiElement;

public abstract class ClauseAbstractPreset extends CodeStylePresetImpl {
    ClauseAbstractPreset(String id, String name) {
        super(id, name);
    }

    @Override
    public boolean accepts(BasePsiElement<?> psiElement) {
        return psiElement.getElementType().is(ElementTypeAttribute.CLAUSE);
    }

    protected Spacing getSpacing(BasePsiElement<?> psiElement, boolean shouldWrap) {
        if (shouldWrap) {
            return SPACING_LINE_BREAK;
        } else {
            PsiElement previousPsiElement = psiElement.getPrevSibling();
            if (previousPsiElement instanceof TokenPsiElement previousToken) {
                SharedTokenTypeBundle sharedTokenTypes = psiElement.getLanguage().getSharedTokenTypes();
                TokenType tokenType = previousToken.getTokenType();
                return tokenType ==  sharedTokenTypes.getChrLeftParenthesis() ?
                        SPACING_NO_SPACE :
                        SPACING_ONE_SPACE;

            }
            return SPACING_ONE_SPACE;
        }
    }
}
