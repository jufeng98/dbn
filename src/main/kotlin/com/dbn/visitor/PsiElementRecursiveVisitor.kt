package com.github.xiaolyuh.visitor

import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiRecursiveVisitor
import com.intellij.psi.search.PsiElementProcessor
import com.intellij.psi.util.PsiTreeUtil

/**
 * @author yudong
 */
abstract class PsiElementRecursiveVisitor : PsiRecursiveVisitor, PsiElementVisitor() {

    override fun visitElement(root: PsiElement) {
        PsiTreeUtil.processElements(root, object : PsiElementProcessor.FindElement<PsiElement>() {
            override fun execute(element: PsiElement): Boolean {
                ProgressIndicatorProvider.checkCanceled()
                return visitEachElement(element)
            }
        })
    }

    /**
     * 递归迭代所有元素.注意,不能修改psiElement,否则抛异常
     * @return 返回false则停止迭代
     */
    abstract fun visitEachElement(psiElement: PsiElement): Boolean

}
