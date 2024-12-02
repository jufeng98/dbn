package com.dbn.code.common.style.presets.clause;

import com.dbn.language.common.psi.BasePsiElement;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class ClauseChopDownIfLongPreset extends ClauseAbstractPreset {
    public ClauseChopDownIfLongPreset() {
        super("chop_down_if_long", "Chop down if long");
    }

    @Override
    @Nullable
    public Wrap getWrap(BasePsiElement<?> psiElement, CodeStyleSettings settings) {
        boolean shouldWrap = psiElement.approximateLength() > settings.getRightMargin(psiElement.getLanguage());
        return shouldWrap ? WRAP_ALWAYS : WRAP_NONE;

    }

    @Override
    @Nullable
    public Spacing getSpacing(BasePsiElement<?> psiElement, CodeStyleSettings settings) {
        boolean shouldChopDown = psiElement.approximateLength() > settings.getRightMargin(psiElement.getLanguage());
        return getSpacing(psiElement, shouldChopDown);
    }
}