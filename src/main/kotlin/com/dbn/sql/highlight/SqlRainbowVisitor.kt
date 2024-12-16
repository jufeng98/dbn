package com.dbn.sql.highlight

import com.dbn.sql.psi.SqlColumnAlias
import com.dbn.sql.psi.SqlColumnName
import com.dbn.sql.psi.SqlFunctionName
import com.dbn.sql.psi.SqlTableName
import com.intellij.codeInsight.daemon.RainbowVisitor
import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * @author yudong
 */
class SqlRainbowVisitor : RainbowVisitor() {
    override fun suitableForFile(file: PsiFile): Boolean {
        // RainbowVisitor 的配置默认是关闭的,因此改用 Annotator 进行着色
        // return file is SqlFile
        return false
    }

    override fun visit(element: PsiElement) {
        when (element) {
            is SqlColumnName -> {
                addInfo(element)
            }

            is SqlColumnAlias -> {
                addInfo(element)
            }

            is SqlTableName -> {
                addInfo(element)
            }

            is SqlFunctionName -> {
                addInfo(element)
            }
        }
    }

    private fun addInfo(element: PsiElement) {
        addInfo(
            getInfo(
                element.containingFile,
                element,
                element.javaClass.name,
                null
            )
        )
    }

    override fun clone(): HighlightVisitor {
        return SqlRainbowVisitor()
    }

}
