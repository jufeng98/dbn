package com.dbn.sql.reference

import com.dbn.cache.CacheDbColumn
import com.dbn.cache.CacheDbTable
import com.dbn.navigation.psi.DbnToolWindowPsiElement
import com.dbn.navigation.psi.DbnToolWindowPsiElement.Companion.getFirstConnCacheDbTables
import com.dbn.sql.psi.*
import com.dbn.utils.SqlUtils
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import java.util.*
import java.util.stream.Collectors

/**
 * @author yudong
 */
class TableOrColumnPsiReference(
    sqlStatement: SqlStatement,
    private val sqlTableNames: List<SqlTableName>,
    private val sqlColumnName: SqlColumnName?,
    textRange: TextRange,
) :
    PsiReferenceBase<PsiElement>(sqlStatement, textRange) {

    override fun resolve(): PsiElement {
        val tableNames = sqlTableNames.map { it.name }.toSet()
        return if (sqlColumnName != null) {
            val columnAlias = SqlUtils.getColumnAliasIfInOrderGroupBy(sqlColumnName)
            if (columnAlias != null) {
                return columnAlias
            }

            DbnToolWindowPsiElement(tableNames, sqlColumnName.name, sqlTableNames[0].node)
        } else {
            DbnToolWindowPsiElement(tableNames, null, sqlTableNames[0].node)
        }
    }

    override fun getVariants(): Array<Any> {
        return if (sqlColumnName == null) {
            findTableNames()
        } else {
            findColumnNames()
        }
    }

    private fun findTableNames(): Array<Any> {
        val project = sqlTableNames.iterator().next().project
        val tableMap = getFirstConnCacheDbTables(project) ?: return arrayOf()

        return tableMap.values
            .map { it: CacheDbTable ->
                LookupElementBuilder
                    .create(it.name)
                    .withTypeText(it.comment, true)
                    .bold()
            }
            .toTypedArray()
    }

    private fun findColumnNames(): Array<Any> {
        val project = sqlColumnName!!.project
        val tableMap = getFirstConnCacheDbTables(project) ?: return arrayOf()
        val insertStr =
            if (PsiTreeUtil.getParentOfType(sqlColumnName, SqlResultColumn::class.java) != null) {
                ", "
            } else if (PsiTreeUtil.getParentOfType(sqlColumnName, SqlJoinConstraint::class.java) != null) {
                " = "
            } else {
                ""
            }

        val lookupElementBuilders: Array<Any> = sqlTableNames.stream()
            .map {
                tableMap[it.text]
            }
            .filter { Objects.nonNull(it) }
            .map {
                val cacheDbColumnMap: Map<String, CacheDbColumn> = it!!.cacheDbColumnMap
                cacheDbColumnMap.values
                    .map { dbColumn ->
                        val typeText = (dbColumn.cacheDbDataType.qualifiedName + " " + dbColumn.columnDefault
                                + " " + dbColumn.columnComment)
                        LookupElementBuilder.create(dbColumn.name)
                            .withInsertHandler { context: InsertionContext, _: LookupElement? ->
                                val editor = context.editor
                                val document = editor.document
                                context.commitDocument()
                                document.insertString(context.tailOffset, insertStr)
                                editor.caretModel.moveToOffset(context.tailOffset)
                            }
                            .withTypeText(typeText, true)
                            .bold()
                    }
            }
            .collect(Collectors.toList())
            .flatten()
            .toTypedArray()

        if (SqlUtils.isInOrderGroupBy(sqlColumnName)) {
            val sqlColumnAliases = SqlUtils.getSqlColumnAliases(sqlColumnName)
            val builderList: Array<Any> = sqlColumnAliases
                .map {
                    val typeText = PsiTreeUtil.getPrevSiblingOfType(it, SqlColumnExpr::class.java)?.text + " 列别名"
                    LookupElementBuilder.create(it.name!!)
                        .withTypeText(typeText, true)
                        .bold()
                }
                .toTypedArray()
            return arrayOf(*lookupElementBuilders, *builderList)
        }

        return lookupElementBuilders
    }
}
