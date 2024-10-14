package com.dbn.suppress

import com.dbn.sql.SqlLanguage
import com.intellij.codeInsight.highlighting.HighlightErrorFilter
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.xml.XmlText

class MyHighlightErrorFilter : HighlightErrorFilter() {
    override fun shouldHighlightErrorElement(element: PsiErrorElement): Boolean {
        val language = element.language
        val project = element.project

        if (language === SqlLanguage.INSTANCE) {
            // 忽略MyBatis xml里的sql语法错误
            val injectionHost = InjectedLanguageManager.getInstance(project).getInjectionHost(element)
            return injectionHost !is XmlText
        }

        return true
    }
}
