package com.dbn.sql.structure

import com.dbn.sql.SqlIcons
import com.dbn.sql.SqlPsiElement
import com.dbn.sql.parser.SqlFile
import com.dbn.sql.psi.SqlColumnName
import com.dbn.sql.psi.SqlResultColumn
import com.dbn.sql.psi.SqlTableName
import com.dbn.sql.psi.SqlTableOrSubquery
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.search.PsiElementProcessor
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon

/**
 * @author yudong
 */
class SqlStructureViewTreeElement(private val sqlFile: SqlFile, private val sqlPsiElement: SqlPsiElement?) :
    StructureViewTreeElement {

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                return sqlPsiElement?.text
            }

            override fun getIcon(unused: Boolean): Icon {
                return SqlIcons.FILE
            }

        }
    }

    override fun getChildren(): Array<TreeElement> {
        if (sqlPsiElement != null) {
            return emptyArray()
        }

        val list = mutableListOf<TreeElement>()

        PsiTreeUtil.processElements(sqlFile, object : PsiElementProcessor.FindElement<PsiElement>() {
            override fun execute(element: PsiElement): Boolean {
                ProgressIndicatorProvider.checkCanceled()
                if (element is SqlTableName && PsiTreeUtil.getParentOfType(
                        element,
                        SqlTableOrSubquery::class.java
                    ) != null
                ) {
                    list.add(SqlStructureViewTreeElement(sqlFile, element as SqlPsiElement))
                } else if (element is SqlColumnName && PsiTreeUtil.getParentOfType(
                        element,
                        SqlResultColumn::class.java
                    ) != null
                ) {
                    list.add(SqlStructureViewTreeElement(sqlFile, element as SqlPsiElement))
                }
                return true
            }
        })

        return list.toTypedArray()
    }

    override fun navigate(requestFocus: Boolean) {
        sqlPsiElement?.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean {
        return sqlPsiElement?.canNavigate() ?: false
    }

    override fun canNavigateToSource(): Boolean {
        return sqlPsiElement?.canNavigateToSource() ?: false
    }

    override fun getValue(): Any {
        return sqlPsiElement ?: sqlFile
    }
}
