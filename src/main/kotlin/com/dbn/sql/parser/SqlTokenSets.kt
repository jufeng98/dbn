package com.dbn.sql.parser

import com.dbn.sql.psi.SqlTypes
import com.intellij.psi.tree.TokenSet

/**
 * @author yudong
 */
object SqlTokenSets {
    @JvmField
    val STRING_LITERALS: TokenSet = TokenSet.create(SqlTypes.STRING)

    @JvmField
    val SQL_COMMENT: TokenSet = TokenSet.create(SqlTypes.COMMENT, SqlTypes.BLOCK_COMMENT)
}
