package com.dbn.sql.structure

import com.dbn.sql.parser.SqlFile
import com.dbn.sql.psi.SqlColumnName
import com.dbn.sql.psi.SqlTableName
import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.openapi.editor.Editor

/**
 * @author yudong
 */
class SqlStructureViewModel(sqlFile: SqlFile, editor: Editor?) :
    StructureViewModelBase(sqlFile, editor, SqlStructureViewTreeElement(sqlFile, null)), ElementInfoProvider {

    init {
        withSuitableClasses(SqlTableName::class.java, SqlColumnName::class.java)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
        return false
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean {
        return false
    }
}
