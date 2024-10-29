package com.dbn.sql.annotator

import com.dbn.execution.statement.MySqlStatementGutterRenderer
import com.dbn.sql.psi.SqlRoot
import com.dbn.vfs.file.MySqlDBConsoleVirtualFile
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.psi.PsiElement

/**
 * @author yudong
 */
class SqlConsoleAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is SqlRoot) {
            return
        }

        val virtualFile = element.containingFile.virtualFile
        if (virtualFile !is MySqlDBConsoleVirtualFile) {
            return
        }

        val injectionHost = InjectedLanguageManager.getInstance(element.project).getInjectionHost(element)
        if (injectionHost != null) {
            return
        }

        val gutterRenderer = MySqlStatementGutterRenderer(element)

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .gutterIconRenderer(gutterRenderer)
            .create()
    }


}
