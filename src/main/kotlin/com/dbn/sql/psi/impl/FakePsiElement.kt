package com.dbn.sql.psi.impl

import com.dbn.utils.TooltipUtils.showTooltip
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.psi.PsiElement

/**
 * @author yudong
 */
class FakePsiElement(private val element: PsiElement) : ASTWrapperPsiElement(
    element.node
) {
    override fun navigate(requestFocus: Boolean) {
        showTooltip("无法找到要跳转的声明", element.project)
    }
}
