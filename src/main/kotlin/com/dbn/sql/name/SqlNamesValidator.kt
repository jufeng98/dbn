package com.dbn.sql.name

import com.dbn.sql.parser.SqlAdapter
import com.dbn.sql.psi.SqlTypes
import com.dbn.utils.SqlUtils
import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project

/**
 * @author yudong
 */
class SqlNamesValidator : NamesValidator {
    private val myLexer = SqlAdapter()

    override fun isKeyword(name: String, project: Project): Boolean {
        myLexer.start(name)
        val tokenType = myLexer.tokenType
        return SqlUtils.isKeyword(tokenType) && myLexer.tokenEnd == name.length
    }

    override fun isIdentifier(name: String, project: Project): Boolean {
        myLexer.start(name)
        return myLexer.tokenType == SqlTypes.ID && myLexer.tokenEnd == name.length
    }
}
