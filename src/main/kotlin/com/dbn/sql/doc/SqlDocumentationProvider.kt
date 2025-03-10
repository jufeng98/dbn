package com.dbn.sql.doc

import com.dbn.browser.DatabaseBrowserManager
import com.dbn.cache.CacheDbColumn
import com.dbn.cache.CacheDbTable
import com.dbn.navigation.psi.DbnToolWindowPsiElement
import com.dbn.`object`.DBColumn
import com.dbn.`object`.DBTable
import com.dbn.`object`.common.DBObjectPsiElement
import com.dbn.utils.SqlUtils.convertName
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup.*
import com.intellij.psi.PsiElement
import java.util.*
import java.util.stream.Collectors

/**
 * @author yudong
 */
class SqlDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        val project = element.project

        val cacheDbTableMap = DbnToolWindowPsiElement.getFirstConnCacheDbTables(project) ?: return null

        val browserManager = DatabaseBrowserManager.getInstance(project)
        val databaseType = browserManager.getFirstConnectionType(project)

        if (element is DBObjectPsiElement) {
            val dbObject = element.`object`
            if (dbObject is DBTable) {
                val tableName = convertName(dbObject.name, databaseType)
                val cacheDbTable = cacheDbTableMap[tableName] ?: return null

                return generateTableDoc(cacheDbTable)
            }

            if (dbObject is DBColumn) {
                val columnName = convertName(dbObject.name, databaseType)
                val dbTable = dbObject.getParentObject<DBTable>()

                val tableName = convertName(dbTable.name, databaseType)
                val cacheDbTable = cacheDbTableMap[tableName] ?: return null

                val cacheDbColumn = cacheDbTable.cacheDbColumnMap[columnName] ?: return null

                return generateColumnDoc(cacheDbTable, cacheDbColumn)
            }
        }

        if (element is DbnToolWindowPsiElement) {
            if (element.columnName == null) {
                val tableName = convertName(element.tableNames.iterator().next(), databaseType)
                val cacheDbTable = cacheDbTableMap[tableName] ?: return null

                return generateTableDoc(cacheDbTable)
            } else {
                val columnDocList = element.tableNames.stream()
                    .map {
                        val cacheDbTable = cacheDbTableMap[it] ?: return@map null

                        val columnName = convertName(element.columnName, databaseType)
                        val cacheDbColumn = cacheDbTable.cacheDbColumnMap[columnName] ?: return@map null

                        generateColumnDoc(cacheDbTable, cacheDbColumn)
                    }
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())

                return if (columnDocList.isEmpty()) {
                    "<div style='color:red'>错误: 在${element.tableNames}中未能解析列${element.columnName}!</div>"
                } else if (columnDocList.size == 1) {
                    columnDocList[0]
                } else {
                    "<div style='color:red'>错误: 解析到多个列${element.columnName}!</div>" +
                            columnDocList.joinToString(separator = "<br/>")
                }
            }
        }

        return null
    }

    private fun generateColumnDoc(table: CacheDbTable, column: CacheDbColumn): String {
        return buildString {
            append("<span>")
            append(column.name)
            append(" ")
            append(column.cacheDbDataType.qualifiedName)
            append(" ")
            append(column.columnDefault)
            append(" ")
            append(column.columnComment)
            append(" ")
            append("</span>")

            append("<span style='color:gray'>")
            append(table.name)
            append("(")
            append(table.comment)
            append(")")
            append("</span>")
        }
    }

    private fun generateTableDoc(table: CacheDbTable): String {
        return buildString {
            append(DEFINITION_START)
            append(table.name)
            append("(")
            append(table.comment)
            append(")")
            append(DEFINITION_END)

            append(SECTIONS_START)

            for (column in table.cacheDbColumnMap.values) {
                append("<tr><td valign='top' class='section' style='color:black'><p>")
                append(column.name)
                append("</p>")
                append("</td><td valign='top' style='color:gray'>")
                append(column.cacheDbDataType.qualifiedName)
                append(" ")
                append(column.columnDefault)
                append(" ")
                append(column.columnComment)
                append("</td>")
                append("</tr>\r\n")
            }

            append("<tr><td colspan='2'><hr/></td></tr>")

            for (index in table.cacheDbIndexMap.values) {
                append("<tr><td valign='top' class='section' style='color:black'><p>")
                append(index.name)
                append("</p>")
                append("</td><td valign='top' style='color:gray'>")
                append(index.columnNames.joinToString(", "))
                append("</td>")
                append("</tr>\r\n")
            }

            append(SECTIONS_END)
        }
    }
}
