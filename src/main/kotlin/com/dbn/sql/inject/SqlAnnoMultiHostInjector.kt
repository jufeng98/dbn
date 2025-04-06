package com.dbn.sql.inject

import com.dbn.sql.SqlLanguage
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil

/**
 * @author yudong
 */
class SqlAnnoMultiHostInjector : MultiHostInjector {
    private val mybatisAnnoSet = setOf(
        "org.apache.ibatis.annotations.Select",
        "org.apache.ibatis.annotations.Insert",
        "org.apache.ibatis.annotations.Delete",
        "org.apache.ibatis.annotations.Update"
    )

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (!shouldInject(context)) {
            return
        }

        if (context is PsiPolyadicExpression) {
            val operands = context.operands

            registrar.startInjecting(SqlLanguage.INSTANCE)
            for (operand in operands) {
                val textRange = innerRangeStrippingQuotes(operand)
                registrar.addPlace(null, null, (operand as PsiLanguageInjectionHost), textRange)
            }
            registrar.doneInjecting()
        } else {
            val parent = context.parent
            if (parent is PsiPolyadicExpression) {
                return
            }

            val textRange = innerRangeStrippingQuotes(context)
            registrar
                .startInjecting(SqlLanguage.INSTANCE)
                .addPlace(null, null, (context as PsiLanguageInjectionHost), textRange)
                .doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>?> {
        return listOf(PsiLiteralExpression::class.java, PsiPolyadicExpression::class.java)
    }

    private fun shouldInject(context: PsiElement): Boolean {
        if (context !is PsiLiteralExpression && context !is PsiPolyadicExpression) {
            return false
        }

        val psiAnnotation = PsiTreeUtil.getParentOfType(
            context,
            PsiAnnotation::class.java
        ) ?: return false

        val contains = mybatisAnnoSet.contains(psiAnnotation.qualifiedName)
        if (!contains) {
            return false
        }

        val psiClass = PsiUtil.getTopLevelClass(psiAnnotation)
        return psiClass != null
    }


    private fun innerRangeStrippingQuotes(context: PsiElement): TextRange {
        val textRange = context.textRange
        val textRangeTmp = textRange.shiftLeft(textRange.startOffset)
        return TextRange(textRangeTmp.startOffset + 1, textRangeTmp.endOffset - 1)
    }

}
