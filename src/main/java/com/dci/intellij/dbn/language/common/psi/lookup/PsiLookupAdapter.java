package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class PsiLookupAdapter {
    private boolean assertResolved = false;

    public abstract boolean matches(BasePsiElement element);

    public abstract boolean accepts(BasePsiElement element);

    @Nullable
    public final BasePsiElement findInParentScopeOf(BasePsiElement source) {
        AtomicReference<BasePsiElement> psiElement = new AtomicReference<>();
        PsiScopeVisitor.visit(source, scope -> {
            BasePsiElement result = scope.findPsiElement(PsiLookupAdapter.this, 10);
            if (result == scope) result = null;

            psiElement.set(result);
            return result != null;
        });

        return psiElement.get();
    }

    public final BasePsiElement findInScope(@NotNull BasePsiElement scope) {
        return scope.findPsiElement(this, 100);
    }

    public final BasePsiElement findInElement(@NotNull BasePsiElement element) {
        return element.findPsiElement(this, 100);
    }


    public final void collectInParentScopeOf(@NotNull BasePsiElement source, Consumer<? super BasePsiElement> consumer) {
        PsiScopeVisitor.visit(source, scope -> {
            scope.collectPsiElements(PsiLookupAdapter.this, 1, consumer);
            return false;
        });
    }

    public final void collectInScope(@NotNull BasePsiElement scope, @NotNull Consumer<? super BasePsiElement> consumer) {
        BasePsiElement collectScope = scope.isScopeBoundary() ? scope : scope.getEnclosingScopeElement();
        if (collectScope == null) return;

        collectScope.collectPsiElements(this, 100, consumer);
    }

    public final void collectInElement(@NotNull BasePsiElement element, @NotNull Consumer<? super BasePsiElement> consumer) {
        element.collectPsiElements(this, 100, consumer);
    }
}
