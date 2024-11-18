package com.dbn.sql.usage

import com.dbn.sql.psi.SqlNamedElement
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls

/**
 * @author yudong
 */
class SqlFindUsageProvider : FindUsagesProvider {
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is SqlNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): @NonNls String? {
        return null
    }

    override fun getType(element: PsiElement): @Nls String {
        return "别名 "
    }

    override fun getDescriptiveName(element: PsiElement): @Nls String {
        if (element !is PsiNamedElement) {
            return "不可用"
        }

        val name = element.name ?: return "佚名"

        return name
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): @Nls String {
        return getDescriptiveName(element)
    }
}
