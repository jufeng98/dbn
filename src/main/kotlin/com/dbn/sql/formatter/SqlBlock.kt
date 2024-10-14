package com.dbn.sql.formatter

import com.dbn.code.common.style.presets.CodeStylePreset
import com.dbn.sql.SqlLanguage
import com.dbn.sql.codestyle.SqlCodeStyleSettings
import com.dbn.sql.psi.*
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType

/**
 * @author yudong
 */
class SqlBlock(
    private val settings: CodeStyleSettings,
    private val customSettings: SqlCodeStyleSettings,
    val psiElement: PsiElement,
) : ASTBlock {
    private val childBlocks: MutableList<Block> by lazy {
        initChildBlocks()
    }

    private fun initChildBlocks(): MutableList<Block> {
        val list = mutableListOf<Block>()
        var child = psiElement.firstChild
        while (child != null) {
            if (child !is PsiWhiteSpace && child.textLength > 0) {
                val childBlock = SqlBlock(settings, customSettings, child)
                list.add(childBlock)
            }
            child = child.nextSibling
        }
        return list
    }

    override fun getTextRange(): TextRange {
        return psiElement.textRange
    }

    override fun getSubBlocks(): MutableList<Block> {
        return childBlocks
    }

    override fun getWrap(): Wrap? {
        return CodeStylePreset.WRAP_NONE
    }

    override fun getIndent(): Indent? {
        if (psiElement is SqlBinaryAndExpr || psiElement is SqlBinaryEqualityExpr) {
            val sqlBinaryAndExpr = PsiTreeUtil.getParentOfType(psiElement, SqlBinaryAndExpr::class.java)
            if (sqlBinaryAndExpr != null) {
                return Indent.getSpaceIndent(0)
            }
        }

        if (psiElement is SqlJoinOperator) {
            return Indent.getSpaceIndent(0)
        }

        if (psiElement is SqlResultColumn
            || psiElement is SqlTableOrSubquery
            || psiElement is SqlBinaryAndExpr
            || psiElement is SqlBinaryEqualityExpr
            || psiElement is SqlOrderingTerm
            || psiElement is SqlGroupingTerm
            || psiElement is PsiComment
        ) {
            val indentOptions = settings.getLanguageIndentOptions(SqlLanguage.INSTANCE)
            return Indent.getSpaceIndent(indentOptions.INDENT_SIZE)
        }

        return Indent.getSpaceIndent(0)
    }

    override fun getAlignment(): Alignment? {
        return Alignment.createAlignment()
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        val leftBlock = child1 as SqlBlock?
        val leftPsiElement = leftBlock?.psiElement

        val rightBlock = child2 as SqlBlock
        val rightPsiElement = rightBlock.psiElement

        if (leftPsiElement == null) {
            return null
        }

        if (leftPsiElement is PsiComment || rightPsiElement is PsiComment) {
            return null
        }

        if (leftPsiElement.elementType == SqlTypes.COMMENT || rightPsiElement.elementType == SqlTypes.COMMENT) {
            return null
        }

        var spacing = lineFeed(leftPsiElement, rightPsiElement)
        if (spacing != null) {
            return spacing
        }

        spacing = removeSpaceBetween(leftPsiElement, rightPsiElement)
        if (spacing != null) {
            return spacing
        }

        spacing = addSpaceBetween(leftPsiElement, rightPsiElement)
        if (spacing != null) {
            return spacing
        }

        return Spacing.createSpacing(0, 2, 0, false, 0)
    }

    private fun addSpaceBetween(leftPsiElement: PsiElement, rightPsiElement: PsiElement): Spacing? {
        val need = leftPsiElement.elementType == SqlTypes.COMMA
                || rightPsiElement.elementType == SqlTypes.EQ
                || leftPsiElement.elementType == SqlTypes.EQ
                || leftPsiElement.elementType == SqlTypes.FROM
                || leftPsiElement.elementType == SqlTypes.AS
                || leftPsiElement is SqlJoinOperator

        if (leftPsiElement is SqlColumnExpr && rightPsiElement.elementType == SqlTypes.AS
            || leftPsiElement is SqlColumnExpr && rightPsiElement is SqlColumnAlias
            || leftPsiElement is SqlFunctionExpr && rightPsiElement is SqlColumnAlias
        ) {
            val sqlSelectStmt = PsiTreeUtil.getParentOfType(leftPsiElement, SqlSelectStmt::class.java)!!
            val compoundResultColumn = sqlSelectStmt.compoundResultColumn!!
            val sqlResultColumnList = PsiTreeUtil.getChildrenOfType(compoundResultColumn, SqlResultColumn::class.java)!!
            val maxLength = sqlResultColumnList.map { it.expr!! }.maxOf { it.textLength }
            val width = maxLength - leftPsiElement.textLength + 1
            return Spacing.createSpacing(width, width, 0, false, 0)
        }

        if (need) {
            return if (customSettings.spaceBetweenSymbol) {
                Spacing.createSpacing(1, 1, 0, false, 0)
            } else {
                Spacing.createSpacing(0, 0, 0, false, 0)
            }
        }

        return null
    }

    private fun removeSpaceBetween(leftPsiElement: PsiElement, rightPsiElement: PsiElement): Spacing? {
        val need = leftPsiElement is SqlTableName && rightPsiElement.elementType == SqlTypes.DOT
                || leftPsiElement.elementType == SqlTypes.DOT && rightPsiElement is SqlColumnName
                || leftPsiElement is SqlOrderingTerm && rightPsiElement.elementType == SqlTypes.COMMA

        if (need) {
            return Spacing.createSpacing(0, 0, 0, false, 0)
        }

        return null
    }

    private fun lineFeed(leftPsiElement: PsiElement, rightPsiElement: PsiElement): Spacing? {
        val need = leftPsiElement.elementType == SqlTypes.SELECT && rightPsiElement is SqlCompoundResultColumn
                || leftPsiElement.elementType == SqlTypes.COMMA && rightPsiElement is SqlResultColumn
                || leftPsiElement.elementType == SqlTypes.FROM
                || leftPsiElement.elementType == SqlTypes.WHERE
                || rightPsiElement.elementType == SqlTypes.ORDER
                || leftPsiElement.elementType == SqlTypes.BY && rightPsiElement is SqlOrderingTerm
                || leftPsiElement.elementType == SqlTypes.BY && rightPsiElement is SqlGroupingTerm
                || rightPsiElement.elementType == SqlTypes.AND
                || rightPsiElement.elementType == SqlTypes.GROUP
                || leftPsiElement is SqlCompoundResultColumn
                || leftPsiElement is SqlResultColumn && rightPsiElement.elementType == SqlTypes.FROM
                || leftPsiElement is SqlJoinClause
                || leftPsiElement is SqlGroupingTerm
                || rightPsiElement is SqlJoinOperator
                || leftPsiElement is SqlRoot

        if (need) {
            return Spacing.createSpacing(0, 0, 1, false, 0)
        }

        return null
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val subBlocks: List<Block> = subBlocks
        if (newChildIndex > subBlocks.size - 1) {
            return ChildAttributes(Indent.getNoneIndent(), Alignment.createAlignment())
        } else {
            val child = getSubBlocks()[newChildIndex]
            return ChildAttributes(child.indent, child.alignment)
        }
    }

    override fun isIncomplete(): Boolean {
        return false
    }

    override fun isLeaf(): Boolean {
        return psiElement.node.firstChildNode == null
    }

    override fun getNode(): ASTNode? {
        return psiElement.node
    }

    override fun toString(): String {
        return "psiElement:$psiElement"
    }

}
