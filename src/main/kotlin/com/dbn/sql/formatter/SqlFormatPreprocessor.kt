package com.dbn.sql.formatter

import com.dbn.sql.SqlLanguage
import com.dbn.sql.codestyle.SqlCaseStyle
import com.dbn.sql.codestyle.SqlCodeStyleSettings
import com.dbn.utils.SqlUtils
import com.intellij.application.options.CodeStyle
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessorHelper
import com.intellij.psi.impl.source.codeStyle.PreFormatProcessor
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.DocumentUtil

/**
 * @author yudong
 */
class SqlFormatPreprocessor : PreFormatProcessor {
    override fun process(node: ASTNode, range: TextRange): TextRange {
        val psiElement = node.psi
        if (psiElement == null || !psiElement.isValid) {
            return range
        }

        val file = psiElement.containingFile
        val rootSettings: CodeStyleSettings = CodeStyle.getSettings(file)
        val customSettings = rootSettings.getCustomSettings(
            SqlCodeStyleSettings::class.java
        )

        if (!psiElement.language.isKindOf(SqlLanguage.INSTANCE)) {
            return range
        }

        handleCase(psiElement, customSettings)

        return handleAntiQuote(psiElement, rootSettings, range)
    }

    private fun handleCase(psiElement: PsiElement, customSettings: SqlCodeStyleSettings) {
        val sqlCaseStyle = SqlCaseStyle.getByCode(customSettings.keywordCase)
        if (sqlCaseStyle === SqlCaseStyle.NOT_CHANGE) {
            return
        }

        val children = PsiTreeUtil.findChildrenOfType(psiElement, LeafPsiElement::class.java)
        children.forEach {
            if (!SqlUtils.isKeyword(it.elementType)) {
                return@forEach
            }

            sqlCaseStyle.doModifyKeyword(it)
        }
    }

    private fun handleAntiQuote(psiElement: PsiElement, rootSettings: CodeStyleSettings, range: TextRange): TextRange {
        val postFormatProcessorHelper = PostFormatProcessorHelper(rootSettings.getCommonSettings(SqlLanguage.INSTANCE))
        postFormatProcessorHelper.resultTextRange = range

        val converter = SqlAntiQuotesConverter(psiElement, postFormatProcessorHelper)
        val document = converter.getDocument()
        if (document != null) {
            DocumentUtil.executeInBulk(document, converter)
        }

        return postFormatProcessorHelper.resultTextRange
    }

}
