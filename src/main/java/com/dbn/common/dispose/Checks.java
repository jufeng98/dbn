package com.dbn.common.dispose;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public final class Checks {

    public static boolean allValid(Object ... objects) {
        for (Object object : objects) {
            if (isNotValid(object)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotValid(Object object) {
        return !isValid(object);
    }

    public static boolean isValid(Object object) {
        if (object == null) {
            return false;
        }

        if (object instanceof StatefulDisposable disposable) {
            return !disposable.isDisposed();
        }

        if (object instanceof Project project) {
            return !project.isDisposed();
        }

        if (object instanceof Editor editor) {
            return !editor.isDisposed();
        }

        if (object instanceof FileEditor editor) {
            return editor.isValid();
        }

        if (object instanceof VirtualFile virtualFile) {
            return virtualFile.isValid();
        }

        if (object instanceof PsiElement psiElement) {
            return psiElement.isValid();
        }

        return true;
    }

    @Nullable
    public static <T> T invalidToNull(@Nullable T object) {
        return isValid(object) ? object : null;
    }

    public static boolean isTrue(@Nullable Boolean bool) {
        return bool != null && bool;
    }
}
