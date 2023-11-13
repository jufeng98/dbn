package com.dbn.language.common;


import com.dbn.common.dispose.Failsafe;
import com.dbn.common.ref.WeakRef;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiElementRef<T extends PsiElement> extends WeakRef<T> {
    private PsiElementRef(T psiElement) {
        super(psiElement);
    }

    @Contract("null -> null;!null -> !null;")
    public static <T extends PsiElement> PsiElementRef<T> of(@Nullable T psiElement) {
        return psiElement == null ? null : new PsiElementRef<>(psiElement);
    }

    @Nullable
    public static <T extends PsiElement> T get(@Nullable PsiElementRef<T> ref) {
        return ref == null ? null : ref.get();
    }

    @Nullable
    @Override
    public T get() {
        return super.get();
    }

    @Override
    @NotNull
    public T ensure() {
        return Failsafe.nn(get());
    }

    @Override
    public String toString() {
        T element = get();
        return element == null ? "null" : element.toString();
    }
}
