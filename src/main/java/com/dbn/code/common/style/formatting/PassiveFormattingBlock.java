package com.dbn.code.common.style.formatting;

import com.dbn.code.common.style.presets.CodeStylePreset;
import com.dbn.language.common.PsiElementRef;
import com.intellij.formatting.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PassiveFormattingBlock implements Block {
    private static final  List<Block> EMPTY_LIST = new ArrayList<>(0);
    private final PsiElementRef<?> psiElement;

    public PassiveFormattingBlock(PsiElement psiElement) {
        this.psiElement = PsiElementRef.of(psiElement);
    }

    public PsiElement getPsiElement() {
        return PsiElementRef.get(psiElement);
    }

    @Override
    @NotNull
    public TextRange getTextRange() {
        return getPsiElement().getTextRange();
    }

    @Override
    @NotNull
    public List<Block> getSubBlocks() {
        return EMPTY_LIST;
    }

    @Override
    public Wrap getWrap() {
        return CodeStylePreset.WRAP_NONE;
    }

    @Override
    public Indent getIndent() {
        return Indent.getNoneIndent();
    }

    @Override
    public Alignment getAlignment() {
        return null;
    }

    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return null;
    }

    @Override
    @NotNull
    public ChildAttributes getChildAttributes(int newChildIndex) {
        return new ChildAttributes(Indent.getNoneIndent(), Alignment.createAlignment());
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
