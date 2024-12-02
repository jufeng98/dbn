package com.dbn.code.common.style.presets.statement;

import com.dbn.language.common.psi.BasePsiElement;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class StatementIgnoreSpacingPreset extends StatementAbstractPreset {
    public StatementIgnoreSpacingPreset() {
        super("ignore_spacing", "Ignore");
    }

    @Override
    @Nullable
    public Wrap getWrap(BasePsiElement<?> psiElement, CodeStyleSettings settings) {
        return null;
    }

    @Override
    @Nullable
    public Spacing getSpacing(BasePsiElement<?> psiElement, CodeStyleSettings settings) {
        return null;
    }
}
