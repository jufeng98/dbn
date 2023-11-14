package com.dbn.language.common.element.impl;

import com.dbn.language.common.element.ElementType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class BasicCompositeElement extends CompositeElement {
    public BasicCompositeElement(@NotNull IElementType type) {
        super(type);
    }

    @Override
    protected PsiElement createPsiNoLock() {
        IElementType elementType = getElementType();
        if (elementType instanceof ElementType) {
            ElementType et = (ElementType) elementType;
            return et.createPsiElement(this);
        }
        return super.createPsiNoLock();
    }
}
