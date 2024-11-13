package com.dbn.sql.psi.impl

import com.dbn.sql.SqlElementFactory.createSqlColumnAlias
import com.dbn.sql.SqlElementFactory.createSqlTableAlias
import com.dbn.sql.psi.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

/**
 * @author yudong
 */
object SqlPsiImplUtil {
    const val ANTI_QUOTE_CHAR: String = "`"

    @JvmStatic
    fun setName(element: SqlTableAlias, newName: String?): PsiElement {
        val sqlTableAlias = createSqlTableAlias(element.project, newName!!)
        element.replace(sqlTableAlias)
        return sqlTableAlias
    }

    @JvmStatic
    fun setName(element: SqlColumnAlias, newName: String?): PsiElement {
        val sqlColumnAlias = createSqlColumnAlias(element.project, newName!!)
        element.replace(sqlColumnAlias)
        return sqlColumnAlias
    }

    @JvmStatic
    fun getName(element: SqlTableAlias): String {
        return element.text.replace(ANTI_QUOTE_CHAR, "")
    }

    @JvmStatic
    fun getName(element: SqlColumnAlias): String {
        return element.text
    }

    @JvmStatic
    fun getName(element: SqlTableName): String {
        return element.text.replace(ANTI_QUOTE_CHAR, "")
    }

    @JvmStatic
    fun getName(element: SqlColumnName): String {
        return element.text.replace(ANTI_QUOTE_CHAR, "")
    }

    @JvmStatic
    fun getNameIdentifier(element: SqlTableAlias): PsiElement {
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: SqlColumnAlias): PsiElement {
        return element
    }

    @JvmStatic
    fun getReferences(param: SqlMybatisExpr): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(
            param
        )
    }

    @JvmStatic
    fun getReferences(param: SqlStatement): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(
            param
        )
    }
}
