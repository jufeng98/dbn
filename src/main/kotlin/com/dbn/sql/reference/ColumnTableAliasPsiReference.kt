package com.dbn.sql.reference

import com.dbn.sql.psi.impl.FakePsiElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

/**
 * @author yudong
 */
class ColumnTableAliasPsiReference(element: PsiElement, textRange: TextRange, targetPsiElement: PsiElement?) :
    PsiReferenceBase<PsiElement?>(element, textRange) {
    private val targetPsiElement: PsiElement

    init {
        var tmpEle = targetPsiElement
        if (tmpEle == null) {
            tmpEle = FakePsiElement(element)
        }

        this.targetPsiElement = tmpEle
    }


    override fun resolve(): PsiElement {
        return targetPsiElement
    }
}
