package com.dbn.sql.annotator

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * @author yudong
 */
class AddQuoteQuickFix(val element: PsiElement, private val quote: String) : BaseIntentionAction() {
    override fun getFamilyName(): String {
        return text
    }

    override fun getText(): String {
        @Suppress("DialogTitleCapitalization")
        return "添加缺少的引号"
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        ApplicationManager.getApplication().invokeLater {
            WriteCommandAction.writeCommandAction(element.project).run<Exception> {
                val document = PsiDocumentManager.getInstance(element.project).getDocument(element.containingFile)!!
                document.insertString(element.textRange.endOffset, quote)
            }
        }
    }
}
