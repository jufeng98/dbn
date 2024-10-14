package com.dbn.sql.completion

import com.dbn.cache.CacheDbTable
import com.dbn.common.util.Naming
import com.dbn.navigation.psi.DbnToolWindowPsiElement.Companion.getFirstConnCacheDbTables
import com.dbn.sql.parser.SqlFile
import com.dbn.sql.psi.SqlColumnAlias
import com.dbn.sql.psi.SqlColumnName
import com.dbn.sql.psi.SqlStatement
import com.dbn.sql.psi.SqlTableAlias
import com.dbn.utils.SqlUtils
import com.dbn.utils.StringUtil
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import java.util.function.Consumer

/**
 * @author yudong
 */
class SqlCompletionContributor : CompletionContributor() {
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position
        val parent = position.parent

        if (parent.parent is SqlFile) {
            fillSqlTypes(result)
            return
        }

        if (parent is SqlColumnName) {
            fillTableAliases(result, parent)
            return
        }

        if (parent is SqlTableAlias) {
            fillTableAliasOfGenerated(result, parent)
            return
        }

        if (parent is SqlColumnAlias) {
            fillColumnAliasGenerated(result, parent)
        }
    }

    private fun fillColumnAliasGenerated(result: CompletionResultSet, sqlColumnAlias: SqlColumnAlias) {
        val sqlColumnName = SqlUtils.getSqlColumnNameOfAlias(sqlColumnAlias) ?: return

        val aliasName = StringUtil.toCamelCase(sqlColumnName.text)
        val builder = LookupElementBuilder
            .create(aliasName)
            .bold()
        result.addElement(builder)
    }

    private fun fillTableAliasOfGenerated(result: CompletionResultSet, sqlTableAlias: SqlTableAlias) {
        val sqlTableName = SqlUtils.getTableNameOfAlias(sqlTableAlias) ?: return

        val aliasName = Naming.createAliasName(sqlTableName.text)
        val builder = LookupElementBuilder
            .create(aliasName)
            .bold()
        result.addElement(builder)
    }

    private fun fillTableAliases(result: CompletionResultSet, sqlColumnName: SqlColumnName) {
        val sqlStatement = PsiTreeUtil.getParentOfType(sqlColumnName, SqlStatement::class.java)
            ?: return

        val columnTableAlias = SqlUtils.getTableAliasNameOfColumn(sqlColumnName)
        if (columnTableAlias != null) {
            return
        }

        val tableMap = getFirstConnCacheDbTables(sqlColumnName.project)

        val sqlTableAliases = PsiTreeUtil.findChildrenOfType(sqlStatement, SqlTableAlias::class.java)
        sqlTableAliases.forEach(Consumer { sqlTableAlias: SqlTableAlias ->
            val typeName = getAliasDesc(sqlTableAlias, tableMap)
            val builder = LookupElementBuilder.create(sqlTableAlias.text)
                .withInsertHandler { context: InsertionContext, item: LookupElement? ->
                    val editor = context.editor
                    val document = editor.document
                    context.commitDocument()
                    document.insertString(context.tailOffset, "")
                    editor.caretModel.moveToOffset(context.tailOffset)
                }
                .withTypeText(typeName, true)
                .bold()
            result.addElement(builder)
        })
    }

    private fun getAliasDesc(sqlTableAlias: SqlTableAlias, tableMap: Map<String, CacheDbTable>?): String {
        val sqlTableName = SqlUtils.getTableNameOfAlias(sqlTableAlias) ?: return ""

        val tableName = sqlTableName.text
        if (tableMap == null) {
            return tableName
        }

        var typeName = tableName
        val cacheDbTable = tableMap[tableName]
        if (cacheDbTable != null) {
            typeName += "(" + cacheDbTable.comment + ")"
        }
        return typeName
    }

    private fun fillSqlTypes(result: CompletionResultSet) {
        for (suggestion in SQL_TYPE) {
            val builder = LookupElementBuilder.create(suggestion)
                .withInsertHandler { context: InsertionContext, item: LookupElement? ->
                    val editor = context.editor
                    val document = editor.document
                    context.commitDocument()
                    document.insertString(context.tailOffset, " ")
                    editor.caretModel.moveToOffset(context.tailOffset)
                }
                .bold()
            result.addElement(builder)
        }
    }

    companion object {
        private val SQL_TYPE = arrayOf("select", "update", "delete from", "insert into")
    }
}
