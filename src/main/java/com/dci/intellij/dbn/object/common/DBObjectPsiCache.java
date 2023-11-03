package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.common.ref.WeakRefCache;
import com.dci.intellij.dbn.navigation.psi.DBObjectPsiDirectory;
import com.dci.intellij.dbn.navigation.psi.DBObjectPsiFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import static com.dci.intellij.dbn.common.util.Unsafe.cast;

@UtilityClass
public class DBObjectPsiCache {
    private static final WeakRefCache<DBObject, PsiFile> psiFiles = WeakRefCache.weakKey();
    private static final WeakRefCache<DBObject, PsiElement> psiElements = WeakRefCache.weakKey();
    private static final WeakRefCache<DBObject, PsiDirectory> psiDirectories = WeakRefCache.weakKey();

    public static void clear(DBObject object) {
        psiFiles.remove(object);
        psiElements.remove(object);
        psiDirectories.remove(object);
    }

    public static void map(DBObject object, PsiElement psiElement) {
        if (psiElement instanceof PsiDirectory) {
            psiDirectories.set(object, (PsiDirectory) psiElement);
            return;
        }

        if (psiElement instanceof PsiFile) {
            psiFiles.set(object, (PsiFile) psiElement);
            return;
        }

        psiElements.set(object, psiElement);
    }

    @Contract("null -> null;!null -> !null;")
    public static <T extends PsiElement> T asPsiElement(@Nullable DBObject object) {
        return object == null ? null : cast(psiElements.computeIfAbsent(object, o -> new DBObjectPsiElement(o.ref())));
    }

    @Contract("null -> null;!null -> !null;")
    public static PsiFile asPsiFile(@Nullable DBObject object) {
        return object == null ? null : psiFiles.computeIfAbsent(object, o -> new DBObjectPsiFile(o.ref()));
    }

    @Contract("null -> null;!null -> !null;")
    public static PsiDirectory asPsiDirectory(@Nullable DBObject object) {
        return object == null ? null : psiDirectories.computeIfAbsent(object, o -> new DBObjectPsiDirectory(o.ref()));
    }

}
