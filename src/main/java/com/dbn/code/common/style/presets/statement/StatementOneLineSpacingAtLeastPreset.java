package com.dbn.code.common.style.presets.statement;

import com.dbn.language.common.psi.BasePsiElement;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class StatementOneLineSpacingAtLeastPreset extends StatementAbstractPreset {
    public StatementOneLineSpacingAtLeastPreset() {
        super("one_line_at_least", "One line at least");
    }

    @Override
    @Nullable
    public Wrap getWrap(BasePsiElement psiElement, CodeStyleSettings settings) {
        return null;
    }

    @Override
    @Nullable
    public Spacing getSpacing(BasePsiElement psiElement, CodeStyleSettings settings) {
        return SPACING_MIN_ONE_LINE;
    }
}