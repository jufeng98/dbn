package com.dbn.sql.gutter

import com.dbn.connection.mapping.FileConnectionContextManager
import com.dbn.language.common.DBLanguagePsiFile
import com.dbn.language.common.element.impl.NamedElementType
import com.dbn.language.common.psi.BasePsiElement
import com.dbn.language.common.psi.ExecutablePsiElement
import com.dbn.language.sql.SQLLanguage
import com.dbn.sql.psi.SqlTableName
import com.intellij.lang.ASTNode
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * @author yudong
 */
class MockExecutablePsiElement(
    astNode: ASTNode?,
    elementType: NamedElementType?,
    private val sqlRoot: PsiElement,
    private val sql: String?,
) :
    ExecutablePsiElement(
        astNode,
        elementType
    ) {

    override fun getText(): String {
        return ReadAction.compute<String, Exception> {
            sql ?: sqlRoot.text
        }
    }

    override fun prepareStatementText(): String {
        return ReadAction.compute<String, Exception> {
            sql ?: sqlRoot.text
        }
    }


    override fun getFile(): DBLanguagePsiFile {
        val pair = ReadAction.compute<Pair<Project, VirtualFile>, Exception> {
            Pair(sqlRoot.project, sqlRoot.containingFile.virtualFile)
        }
        val project = pair.first
        val virtualFile = pair.second

        val contextManager = FileConnectionContextManager.getInstance(project)
        return DBLanguagePsiFile.createFromText(
            project,
            "test",
            SQLLanguage.INSTANCE.mainLanguageDialect,
            "select 1",
            contextManager.getConnection(virtualFile),
            contextManager.getDatabaseSchema(virtualFile)
        )!!
    }

    override fun isQuery(): Boolean {
        return true
    }

    override fun matches(basePsiElement: BasePsiElement<*>?, matchType: MatchType?): Boolean {
        return true
    }

    fun getIfSingleTableQuery(): String? {
        val sqlTableNames = PsiTreeUtil.findChildrenOfType(sqlRoot, SqlTableName::class.java)
        if (sqlTableNames.isEmpty() || sqlTableNames.size > 1) {
            return null
        }
        return sqlTableNames.iterator().next().text
    }

    fun getCondition(): String? {
        val sql = sqlRoot.text
        val idx = sql.lowercase().indexOf("where")
        if (idx == -1) {
            return null
        }

        return sql.substring(idx + 5)
    }
}
