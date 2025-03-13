package com.dbn.sql.inject

import com.dbn.sql.SqlLanguage
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText

/**
 * @author yudong
 */
class SqlMultiHostInjector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (!shouldInject(context)) {
            return
        }

        registrar.startInjecting(SqlLanguage.INSTANCE)
        val xmlTag = context as XmlTag

        var hasAddPlace = false
        val collection = PsiTreeUtil.findChildrenOfType(xmlTag, XmlText::class.java)
        for (xmlText in collection) {
            val textRange = innerRangeStrippingQuotes(xmlText) ?: continue

            registrar.addPlace(null, null, (xmlText as PsiLanguageInjectionHost), textRange)
            hasAddPlace = true
        }

        if (hasAddPlace) {
            registrar.doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>?> {
        return listOf(XmlTag::class.java)
    }

    private fun shouldInject(context: PsiElement): Boolean {
        if (context !is XmlTag) {
            return false
        }

        val name = context.name
        return "select" == name || "delete" == name || "update" == name || "insert" == name || "sql" == name
    }


    private fun innerRangeStrippingQuotes(context: PsiElement): TextRange? {
        val textRange = context.textRange
        val textRangeTmp = textRange.shiftLeft(textRange.startOffset)
        if (textRangeTmp.endOffset == 0) {
            return null
        }

        return textRangeTmp
    }
}
