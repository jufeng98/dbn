package com.dbn.language.common.psi;

import com.dbn.language.common.element.impl.IdentifierElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class IdentifierDefPsiElement extends IdentifierPsiElement implements PsiNamedElement {
    public IdentifierDefPsiElement(ASTNode astNode, IdentifierElementType elementType) {
        super(astNode, elementType);
    }

    public PsiElement setName(@NotNull @NonNls String name) throws IncorrectOperationException {
        return null;
    }

}
