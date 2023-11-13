package com.dbn.code.common.style.presets.statement;

import com.dbn.language.common.psi.BasePsiElement;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class StatementLineBreakAtLeastPreset extends StatementAbstractPreset {
    public StatementLineBreakAtLeastPreset() {
        super("one_line_break_at_least", "One line break at least");
    }

    @Override
    @Nullable
    public Wrap getWrap(BasePsiElement psiElement, CodeStyleSettings settings) {
        return WRAP_ALWAYS;
    }

    @Override
    @Nullable
    public Spacing getSpacing(BasePsiElement psiElement, CodeStyleSettings settings) {
        return null;
    }
}