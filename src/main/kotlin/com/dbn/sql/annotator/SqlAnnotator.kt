package com.dbn.sql.annotator

import com.dbn.browser.DatabaseBrowserManager
import com.dbn.cache.CacheDbTable
import com.dbn.connection.DatabaseType
import com.dbn.navigation.psi.DbnToolWindowPsiElement.Companion.getFirstConnCacheDbTables
import com.dbn.sql.psi.*
import com.dbn.utils.SqlUtils
import com.dbn.utils.SqlUtils.convertName
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.xml.XmlText
import java.util.*

/**
 * @author yudong
 */
class SqlAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is SqlStringLiteral) {
            analyzeStringQuote(element, holder)
            return
        }

        if (element is SqlColumnName) {
            val builder = holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            builder.textAttributes(DefaultLanguageHighlighterColors.STATIC_FIELD)
            builder.create()
            return
        }

        if (element !is SqlRoot) {
            return
        }

        val project = element.project

        val browserManager = DatabaseBrowserManager.getInstance(project)
        val databaseType = browserManager.getFirstConnectionType(project)

        val injectionHost = InjectedLanguageManager.getInstance(element.project).getInjectionHost(element)
        if (injectionHost !is XmlText) {
            analyzeSemantics(element, holder, databaseType)
            return
        }

        val errorElement = PsiTreeUtil.findChildOfType(element.containingFile, PsiErrorElement::class.java)
        if (errorElement == null) {
            analyzeSemantics(element, holder, databaseType)
            return
        }

        val prevValidSibling = getPrevValidSiblingOfError(errorElement) ?: return

        analyzeSyntax(errorElement, prevValidSibling, holder, element)
    }

    private fun getPrevValidSiblingOfError(element: PsiErrorElement): PsiElement? {
        var prevSibling: PsiElement? = element.prevSibling
        while (prevSibling != null) {
            if (prevSibling !is PsiWhiteSpace) {
                return prevSibling
            }
            prevSibling = prevSibling.prevSibling
        }
        return null
    }

    private fun analyzeStringQuote(element: SqlStringLiteral, holder: AnnotationHolder) {
        val text = element.text
        if (text.length != 1 && text[0] == text[text.length - 1]) return

        @Suppress("DialogTitleCapitalization")
        holder.newAnnotation(HighlightSeverity.ERROR, "缺少右引号")
            .range(element.textRange)
            .withFix(AddQuoteQuickFix(element, text[0].toString()))
            .highlightType(ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
            .create()
    }

    private fun analyzeSyntax(
        errorElement: PsiErrorElement,
        prevSiblingOfError: PsiElement,
        holder: AnnotationHolder,
        sqlRoot: SqlRoot,
    ) {
        if (prevSiblingOfError is SqlCompoundSelectStmt
            && prevSiblingOfError.selectStmtList.isNotEmpty()
            && errorElement.firstChild.elementType == SqlTypes.AS
            && sqlRoot.textRange.contains(prevSiblingOfError.textRange)
        ) {
            val lastElement = SqlUtils.getLastChildElement(prevSiblingOfError)
            @Suppress("DialogTitleCapitalization")
            holder.newAnnotation(HighlightSeverity.ERROR, "有语法错误!")
                .range(lastElement.textRange)
                .highlightType(ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                .create()
            return
        }

        if (prevSiblingOfError.elementType == SqlTypes.COMMA
            && errorElement.firstChild.elementType == SqlTypes.FROM
            && sqlRoot.textRange.contains(prevSiblingOfError.textRange)
        ) {
            @Suppress("DialogTitleCapitalization")
            holder.newAnnotation(HighlightSeverity.ERROR, "多余的逗号")
                .range(prevSiblingOfError.textRange)
                .withFix(RemoveCommaQuickFix(prevSiblingOfError))
                .highlightType(ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                .create()
            return
        }

        if (prevSiblingOfError.elementType == SqlTypes.DOT
            && prevSiblingOfError.prevSibling is SqlCompoundSelectStmt
            && sqlRoot.textRange.contains(prevSiblingOfError.textRange)
        ) {
            @Suppress("DialogTitleCapitalization")
            holder.newAnnotation(HighlightSeverity.ERROR, "多余的句号")
                .range(prevSiblingOfError.textRange)
                .highlightType(ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                .create()
            return
        }
    }

    private fun calcAliasMap(
        sqlCompoundSelectStmts: MutableCollection<SqlCompoundSelectStmt>,
        databaseType: DatabaseType,
    ): MutableMap<String, MutableList<SqlTableAlias>> {
        val aliasMap: MutableMap<String, MutableList<SqlTableAlias>> = mutableMapOf()
        sqlCompoundSelectStmts.forEach { compoundSelectStmt ->
            compoundSelectStmt.selectStmtList.forEach { sqlSelectStmt ->
                val sqlJoinClauses = PsiTreeUtil.findChildrenOfType(sqlSelectStmt, SqlJoinClause::class.java)
                val map = SqlUtils.getAliasMap(sqlJoinClauses)
                map.entries.forEach {
                    val aliasName = convertName(it.key, databaseType)
                    val list = aliasMap[aliasName]
                    if (list == null) {
                        aliasMap[aliasName] = it.value
                    } else {
                        list.addAll(it.value)
                    }
                }
            }
        }

        return aliasMap
    }

    private fun analyzeSemantics(element: SqlRoot, holder: AnnotationHolder, databaseType: DatabaseType) {
        val tableMap = getFirstConnCacheDbTables(element.project) ?: return
        val statement = element.statement

        val sqlCompoundSelectStmts = PsiTreeUtil.findChildrenOfType(statement, SqlCompoundSelectStmt::class.java)

        val aliasMap = calcAliasMap(sqlCompoundSelectStmts, databaseType)

        sqlCompoundSelectStmts.forEach { compoundSelectStmt ->
            compoundSelectStmt?.selectStmtList?.forEach {
                annotatorSelect(it, holder, tableMap, aliasMap, databaseType)
            }
        }

        val updateStmt = statement.updateStmtLimited
        if (updateStmt != null) {
            annotatorUpdate(updateStmt, holder, tableMap, databaseType)
        }

        val sqlInsertStmt = statement.insertStmt
        if (sqlInsertStmt != null) {
            annotatorInsert(sqlInsertStmt, holder, tableMap, databaseType)
        }

        val deleteStmt = statement.deleteStmtLimited
        if (deleteStmt != null) {
            annotatorDelete(deleteStmt, holder, tableMap, databaseType)
        }

    }

    private fun annotatorDelete(
        deleteStmt: SqlDeleteStmtLimited,
        holder: AnnotationHolder,
        tableMap: Map<String, CacheDbTable>,
        databaseType: DatabaseType,
    ) {
        val sqlTableNames = PsiTreeUtil.findChildrenOfType(deleteStmt, SqlTableName::class.java)
        sqlTableNames.forEach {
            annotateTableName(it, holder, tableMap, databaseType)
        }

        val sqlColumnNames = PsiTreeUtil.findChildrenOfType(deleteStmt, SqlColumnName::class.java)
        sqlColumnNames.forEach {
            val sqlCompoundSelectStmt = PsiTreeUtil.getParentOfType(it, SqlCompoundSelectStmt::class.java)
            if (sqlCompoundSelectStmt != null) {
                val aliasMap = calcAliasMap(mutableListOf(sqlCompoundSelectStmt), databaseType)
                sqlCompoundSelectStmt.selectStmtList.forEach { selectStmt ->
                    annotatorSelect(selectStmt, holder, tableMap, aliasMap, databaseType)
                }
                return@forEach
            }

            val tableName = deleteStmt.qualifiedTableName.tableName
            annotateColumnName(it, holder, tableMap, tableName, databaseType)
        }
    }

    private fun annotatorInsert(
        sqlInsertStmt: SqlInsertStmt,
        holder: AnnotationHolder,
        tableMap: Map<String, CacheDbTable>,
        databaseType: DatabaseType,
    ) {
        val sqlTableNames = PsiTreeUtil.findChildrenOfType(sqlInsertStmt, SqlTableName::class.java)
        sqlTableNames.forEach {
            annotateTableName(it, holder, tableMap, databaseType)
        }

        val sqlColumnNames = PsiTreeUtil.findChildrenOfType(sqlInsertStmt, SqlColumnName::class.java)
        sqlColumnNames.forEach {
            val tableName = sqlInsertStmt.tableName
            annotateColumnName(it, holder, tableMap, tableName, databaseType)
        }
    }

    private fun annotatorUpdate(
        sqlUpdateStmtLimited: SqlUpdateStmtLimited,
        holder: AnnotationHolder,
        tableMap: Map<String, CacheDbTable>,
        databaseType: DatabaseType,
    ) {
        val sqlTableNames = PsiTreeUtil.findChildrenOfType(sqlUpdateStmtLimited, SqlTableName::class.java)
        sqlTableNames.forEach {
            annotateTableName(it, holder, tableMap, databaseType)
        }

        val sqlColumnNames = PsiTreeUtil.findChildrenOfType(sqlUpdateStmtLimited, SqlColumnName::class.java)
        sqlColumnNames.forEach {
            val tableName = sqlUpdateStmtLimited.qualifiedTableName.tableName
            annotateColumnName(it, holder, tableMap, tableName, databaseType)
        }
    }

    private fun annotatorSelect(
        sqlSelectStmt: SqlSelectStmt,
        holder: AnnotationHolder,
        tableMap: Map<String, CacheDbTable>,
        aliasMap: MutableMap<String, MutableList<SqlTableAlias>>,
        databaseType: DatabaseType,
    ) {
        val sqlTableNames = PsiTreeUtil.findChildrenOfType(sqlSelectStmt, SqlTableName::class.java)
        sqlTableNames.forEach {
            if (SqlUtils.isColumnTableAlias(it)) {
                annotateColumnTableAlias(it, holder, aliasMap, databaseType)
            } else {
                annotateTableName(it, holder, tableMap, databaseType)
            }
        }

        val sqlColumnNames = PsiTreeUtil.findChildrenOfType(sqlSelectStmt, SqlColumnName::class.java)
        sqlColumnNames.forEach {
            val columnAlias = SqlUtils.getColumnAliasIfInOrderGroupBy(it, databaseType)
            if (columnAlias != null) {
                return@forEach
            }

            annotateColumnName(it, holder, tableMap, aliasMap, databaseType)
        }
    }

    private fun annotateTableName(
        sqlTableName: SqlTableName,
        holder: AnnotationHolder,
        tableMap: Map<String, CacheDbTable>,
        databaseType: DatabaseType,
    ) {
        val name = convertName(sqlTableName.name, databaseType)

        val cacheDbTable = tableMap[name]
        if (cacheDbTable == null) {
            createError("无法解析表名 $name", holder, sqlTableName.textRange)
        }
    }

    private fun annotateColumnTableAlias(
        columnTableAliasName: SqlTableName,
        holder: AnnotationHolder,
        aliasMap: MutableMap<String, MutableList<SqlTableAlias>>,
        databaseType: DatabaseType,
    ) {
        val aliasName = convertName(columnTableAliasName.name, databaseType)

        val sqlTableAliases = aliasMap[aliasName]
        if (sqlTableAliases == null) {
            createError("无法解析表别名 $aliasName", holder, columnTableAliasName.textRange)
        }
    }

    private fun annotateColumnName(
        sqlColumnName: SqlColumnName,
        holder: AnnotationHolder,
        tableMap: Map<String, CacheDbTable>,
        sqlTableName: SqlTableName,
        databaseType: DatabaseType,
    ) {
        val tableName = convertName(sqlTableName.name, databaseType)
        val cacheDbTable = tableMap[tableName] ?: return

        val columnName = convertName(sqlColumnName.name, databaseType)

        val cacheDbColumn = cacheDbTable.cacheDbColumnMap[columnName]
        if (cacheDbColumn == null) {
            createError("无法解析列名 $columnName", holder, sqlColumnName.textRange)
        }
    }

    private fun annotateColumnName(
        sqlColumnName: SqlColumnName,
        holder: AnnotationHolder,
        tableMap: Map<String, CacheDbTable>,
        aliasMap: MutableMap<String, MutableList<SqlTableAlias>>,
        databaseType: DatabaseType,
    ) {
        val columnName = convertName(sqlColumnName.name, databaseType)

        val columnTableAlias = SqlUtils.getTableAliasNameOfColumn(sqlColumnName)
        if (columnTableAlias != null) {
            val sqlTableName = SqlUtils.getTableNameOfAlias(aliasMap, columnTableAlias) ?: return
            val tableName = sqlTableName.name
            val cacheDbTable = tableMap[tableName] ?: return
            val cacheDbColumn = cacheDbTable.cacheDbColumnMap[columnName]
            if (cacheDbColumn == null) {
                createError("无法解析列名 $columnName", holder, sqlColumnName.textRange)
            }
            return
        }

        val columnAlias = SqlUtils.getColumnAliasIfInOrderGroupBy(sqlColumnName, databaseType)
        if (columnAlias != null) {
            return
        }

        val sqlSelectStmtCurrent = PsiTreeUtil.getParentOfType(sqlColumnName, SqlSelectStmt::class.java)
        val sqlJoinClauses = PsiTreeUtil.getChildrenOfTypeAsList(sqlSelectStmtCurrent, SqlJoinClause::class.java)
        val tableNames = SqlUtils.getSqlTableNames(sqlJoinClauses)

        val cacheDbTables = tableNames
            .filter { !SqlUtils.isColumnTableAlias(it) }
            .map { tableMap[it.name] }
            .filter { Objects.nonNull(it) }

        if (cacheDbTables.isEmpty()) {
            return
        }

        val cacheDbColumns = cacheDbTables
            .map { it!!.cacheDbColumnMap[columnName] }
            .filter { Objects.nonNull(it) }

        if (cacheDbColumns.isEmpty()) {
            createError("无法解析列名 $columnName", holder, sqlColumnName.textRange)
            return
        }

        if (cacheDbColumns.size == 1) {
            return
        }

        createError("解析到多个列名 $columnName", holder, sqlColumnName.textRange)
    }

    private fun createError(tip: String, holder: AnnotationHolder, textRange: TextRange) {
        holder.newAnnotation(HighlightSeverity.ERROR, tip)
            .range(textRange)
            .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
            .create()
    }

}
