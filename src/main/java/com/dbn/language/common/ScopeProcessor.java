package com.dbn.language.common;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.jetbrains.annotations.NotNull;

public class ScopeProcessor implements PsiScopeProcessor {
    @Override
    public boolean execute(@NotNull PsiElement psiElement, @NotNull ResolveState resolveState) {
        return false;
    }

    @Override
    public <T> T getHint(@NotNull Key<T> tKey) {
        return null;
    }

    public <T> T getHint(Class<T> hintClass) {
        return null;
    }

    @Override
    public void handleEvent(@NotNull Event event, Object associated) {

    }
}
