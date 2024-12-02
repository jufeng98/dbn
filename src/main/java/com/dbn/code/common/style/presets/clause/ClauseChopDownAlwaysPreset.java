package com.dbn.code.common.style.presets.clause;

import com.dbn.language.common.psi.BasePsiElement;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class ClauseChopDownAlwaysPreset extends ClauseAbstractPreset {
    public ClauseChopDownAlwaysPreset() {
        super("chop_down", "Chop down");
    }

    @Override
    @Nullable
    public Wrap getWrap(BasePsiElement<?> psiElement, CodeStyleSettings settings) {
        return WRAP_ALWAYS;
    }

    @Override
    @Nullable
    public Spacing getSpacing(BasePsiElement<?> psiElement, CodeStyleSettings settings) {
        return SPACING_LINE_BREAK;
    }
}