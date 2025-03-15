package com.dbn.sql.intent

import com.dbn.cache.CacheDbTable
import com.dbn.navigation.psi.DbnToolWindowPsiElement
import com.dbn.sql.psi.SqlResultColumn
import com.dbn.sql.psi.SqlSelectStmt
import com.dbn.sql.psi.SqlStatement
import com.dbn.sql.reference.SqlReferenceContributor
import com.dbn.sql.reference.TableOrColumnPsiReference
import com.dbn.utils.NotifyUtil
import com.dbn.utils.StringUtil
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.util.IntentionName
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import java.awt.datatransfer.StringSelection
import java.util.*

/**
 * @author yudong
 */
class GenerateFieldAction : BaseIntentionAction() {
    override fun getText(): @IntentionName String {
        @Suppress("DialogTitleCapitalization")
        return "生成 Java 类字段定义"
    }

    override fun getFamilyName(): String {
        return text
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        val element = getElement(editor, file)

        PsiTreeUtil.getParentOfType(element, SqlSelectStmt::class.java) ?: return false

        return true
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val element = getElement(editor, file) ?: return

        val cacheDbTableMap = DbnToolWindowPsiElement.getFirstConnCacheDbTables(project) ?: return

        val selectStmt = PsiTreeUtil.getParentOfType(element, SqlSelectStmt::class.java) ?: return

        val sqlReferences = SqlReferenceContributor.SqlPsiReferenceProvider
            .createSqlReferences(selectStmt.parent.parent as SqlStatement)
            .filterIsInstance<TableOrColumnPsiReference>()

        val fieldsStr = sqlReferences
            .map {
                val pair = getName(it, cacheDbTableMap) ?: return@map null

                val fieldName = StringUtil.toCamelCase(pair.first)
                val typeName = pair.second

                return@map if (typeName.contains("bigint")) {
                    "private Long $fieldName;"
                } else if (typeName.contains("int")) {
                    "private Integer $fieldName;"
                } else if (typeName.contains("date") || typeName.contains("time")) {
                    "private Date $fieldName;"
                } else if (typeName.contains("char") || typeName.contains("text")) {
                    "private String $fieldName;"
                } else {
                    "private $typeName $fieldName;"
                }
            }
            .filter { Objects.nonNull(it) }
            .distinct()
            .joinToString("\n")

        if (fieldsStr.isBlank()) {
            return
        }

        CopyPasteManager.getInstance().setContents(StringSelection(fieldsStr))
        NotifyUtil.notifyDbToolWindowInfo(project, "已生成 Java field 字段并放入到剪切板!")
    }

    private fun getName(
        reference: TableOrColumnPsiReference,
        cacheDbTableMap: Map<String, CacheDbTable>,
    ): Pair<String, String>? {
        if (reference.sqlTableNames.size != 1) {
            return null
        }

        val tableName = reference.sqlTableNames[0].name

        val sqlColumnName = reference.sqlColumnName
        val columnName = sqlColumnName?.name ?: return null

        val cacheDbTable = cacheDbTableMap[tableName] ?: return null

        val cacheDbColumn = cacheDbTable.cacheDbColumnMap[columnName] ?: return null

        val sqlResultColumn = PsiTreeUtil.getParentOfType(sqlColumnName, SqlResultColumn::class.java) ?: return null

        val columnAlias = sqlResultColumn.columnAlias
        val name = columnAlias?.name ?: columnName

        return Pair(name, cacheDbColumn.cacheDbDataType.name)
    }

    private fun getElement(editor: Editor, file: PsiFile): PsiElement? {
        val caretModel = editor.caretModel
        val position = caretModel.offset
        return file.findElementAt(position)
    }

    override fun startInWriteAction(): Boolean {
        return false
    }
}
