package com.dbn.code.common.style.presets.clause;

import com.dbn.language.common.SharedTokenTypeBundle;
import com.dbn.language.common.TokenType;
import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.TokenPsiElement;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class ClauseChopDownNeverPreset extends ClauseAbstractPreset {
    public ClauseChopDownNeverPreset() {
        super("do_not_chop_down", "Do not chop down");
    }

    @Override
    @Nullable
    public Wrap getWrap(BasePsiElement<?> psiElement, CodeStyleSettings settings) {
        return WRAP_NONE;
    }

    @Override
    @Nullable
    public Spacing getSpacing(BasePsiElement<?> psiElement, CodeStyleSettings settings) {
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
