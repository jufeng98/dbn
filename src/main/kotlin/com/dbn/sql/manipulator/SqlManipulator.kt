package com.dbn.sql.manipulator

import com.dbn.sql.SqlElementFactory
import com.dbn.sql.psi.SqlColumnName
import com.dbn.sql.psi.SqlStatement
import com.dbn.sql.psi.SqlTableName
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

/**
 * 表别名或者列别名发生重命名时,相关引用到别名的地方也跟着修改
 *
 * @author yudong
 */
class SqlManipulator : AbstractElementManipulator<SqlStatement>() {

    override fun handleContentChange(element: SqlStatement, range: TextRange, newContent: String): SqlStatement {
        val targetElement = element.findElementAt(range.startOffset)

        val parent = targetElement?.parent
        if (parent is SqlColumnName) {
            val newSqlColumnName = SqlElementFactory.createSqlColumnName(element.project, newContent)
            parent.replace(newSqlColumnName)
        } else if (parent is SqlTableName) {
            val newSqlTableName = SqlElementFactory.createSqlTableName(element.project, newContent)
            parent.replace(newSqlTableName)
        }

        return element
    }

}
