package com.dbn.sql.folding

import com.dbn.sql.psi.SqlCompoundResultColumn
import com.dbn.sql.psi.SqlSelectStmt
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * @author yudong
 */
class SqlFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        val psiElementList = PsiTreeUtil.findChildrenOfType(root, PsiComment::class.java)
        psiElementList.forEach {
            descriptors.add(FoldingDescriptor(it.node, it.textRange, null))
        }

        val sqlSelectStmtList = PsiTreeUtil.findChildrenOfType(root, SqlSelectStmt::class.java)
        sqlSelectStmtList.forEach {
            descriptors.add(FoldingDescriptor(it.node, it.textRange, null))

            val compoundResultColumn = it.compoundResultColumn ?: return@forEach
            descriptors.add(FoldingDescriptor(compoundResultColumn.node, compoundResultColumn.textRange, null))
        }

        return descriptors.toTypedArray<FoldingDescriptor>()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        val psiElement = node.psi
        return when (psiElement) {
            is PsiComment -> {
                "// ..."
            }

            is SqlCompoundResultColumn -> {
                "column1, column2 ..."
            }

            is SqlSelectStmt -> {
                "select from ..."
            }

            else -> "..."
        }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return node.psi is PsiComment
    }

}
