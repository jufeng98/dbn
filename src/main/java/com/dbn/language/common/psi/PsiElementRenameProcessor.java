package com.dbn.language.common.psi;

import com.dbn.object.common.DBObjectPsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import org.jetbrains.annotations.NotNull;

public class PsiElementRenameProcessor extends RenamePsiElementProcessor {

    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return element instanceof BasePsiElement || element instanceof DBObjectPsiElement;
    }


}
