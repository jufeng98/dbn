package com.dbn.sql.formatter

import com.dbn.sql.psi.SqlColumnAlias
import com.dbn.sql.psi.SqlColumnName
import com.dbn.sql.psi.SqlTableAlias
import com.dbn.sql.psi.SqlTableName
import com.dbn.sql.psi.impl.SqlPsiImplUtil.ANTI_QUOTE_CHAR
import com.github.xiaolyuh.visitor.PsiElementRecursiveVisitor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessorHelper

/**
 * @author yudong
 */
class SqlAntiQuotesConverter(
    private val myContext: PsiElement,
    private val myPostProcessorHelper: PostFormatProcessorHelper,
) : Runnable, PsiElementRecursiveVisitor() {
    private val myDocument: Document? by lazy {
        val file = myContext.containingFile
        file.viewProvider.document
    }
    private val myDocumentManager: PsiDocumentManager by lazy {
        val project = myContext.project
        PsiDocumentManager.getInstance(project)
    }
    private val myOriginalRange: TextRange by lazy {
        myPostProcessorHelper.resultTextRange
    }

    override fun run() {
        if (myDocument == null) return
        runSimple()
        myDocumentManager.commitDocument(myDocument!!)
    }

    private fun runSimple() {
        if (myDocument == null) return

        myDocumentManager.doPostponedOperationsAndUnblockDocument(myDocument!!)

        myContext.accept(this)
    }

    override fun visitEachElement(psiElement: PsiElement): Boolean {
        if (!myOriginalRange.contains(psiElement.textRange)) {
            return true
        }

        val b = psiElement !is SqlTableName
                && psiElement !is SqlColumnName
                && psiElement !is SqlColumnAlias
                && psiElement !is SqlTableAlias
        if (b) {
            return true
        }

        val child = psiElement.firstChild
        if (child == null || !containsQuoteChars(psiElement)) return true

        val startOffset = psiElement.textRange.startOffset
        val endOffset = psiElement.textRange.endOffset
        replaceString(startOffset, startOffset + 1)
        replaceString(endOffset - 1, endOffset)

        return true
    }

    private fun replaceString(start: Int, end: Int) {
        val mappedStart: Int = myPostProcessorHelper.mapOffset(start)
        val mappedEnd: Int = myPostProcessorHelper.mapOffset(end)
        myDocument!!.replaceString(mappedStart, mappedEnd, "")
        myPostProcessorHelper.updateResultRange(end - start, 0)
    }


    private fun containsQuoteChars(value: PsiElement): Boolean {
        var child = value.firstChild
        while (child != null) {
            if (StringUtil.contains(child.node.chars, ANTI_QUOTE_CHAR)) {
                return true
            }
            child = child.nextSibling
        }
        return false
    }

    fun getDocument(): Document? {
        return myDocument
    }

}
