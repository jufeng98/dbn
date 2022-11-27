package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.common.lookup.Visitor;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.intellij.psi.PsiElement;

import java.util.function.Function;

public abstract class PsiScopeVisitor implements Visitor<BasePsiElement> {

    protected PsiScopeVisitor() {}

    public static void visit(BasePsiElement element, Function<BasePsiElement, Boolean> visitor) {
        new PsiScopeVisitor() {
            @Override
            protected boolean visitScope(BasePsiElement scope) {
                return visitor.apply(scope);
            }
        }.visit(element);
    }

    public final void visit(BasePsiElement element) {
        BasePsiElement scope = element.getEnclosingScopePsiElement();
        while (scope != null) {
            boolean breakTreeWalk = visitScope(scope);
            if (breakTreeWalk || scope.getElementType().isScopeIsolation()) break;

            // LOOKUP
            PsiElement parent = scope.getParent();
            if (parent instanceof BasePsiElement) {
                BasePsiElement basePsiElement = (BasePsiElement) parent;
                scope = basePsiElement.getEnclosingScopePsiElement();

            } else {
                scope = null;
            }
        }
    }

    protected abstract boolean visitScope(BasePsiElement scope);
}
