package com.dbn.sql

import com.dbn.sql.parser.SqlFile
import com.dbn.sql.psi.SqlColumnAlias
import com.dbn.sql.psi.SqlColumnName
import com.dbn.sql.psi.SqlTableAlias
import com.dbn.sql.psi.SqlTableName
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil

/**
 * @author yudong
 */
object SqlElementFactory {

    fun createSqlTableName(project: Project, tableName: String): SqlTableName {
        val psiElement = createSqlElement(project, "select * from $tableName")
        return PsiTreeUtil.findChildOfType(psiElement, SqlTableName::class.java)!!
    }

    fun createSqlTableAlias(project: Project, tableAliasName: String): SqlTableAlias {
        val psiElement = createSqlElement(project, "select * from tmp_table $tableAliasName")
        return PsiTreeUtil.findChildOfType(psiElement, SqlTableAlias::class.java)!!
    }

    fun createSqlColumnName(project: Project, columnName: String): SqlColumnName {
        val psiElement = createSqlElement(project, "select $columnName from tmp_table")
        return PsiTreeUtil.findChildOfType(psiElement, SqlColumnName::class.java)!!
    }

    fun createSqlColumnAlias(project: Project, columnAliasName: String): SqlColumnAlias {
        val psiElement = createSqlElement(project, "select id as $columnAliasName from tmp_table")
        return PsiTreeUtil.findChildOfType(psiElement, SqlColumnAlias::class.java)!!
    }

    fun createSqlElement(project: Project, sql: String): PsiElement {
        val file = createFile(project, sql)
        return file.firstChild
    }

    private fun createFile(project: Project, text: String): SqlFile {
        val name = "dummy.mysql"
        return PsiFileFactory.getInstance(project).createFileFromText(name, SqlLanguage.INSTANCE, text) as SqlFile
    }
}
