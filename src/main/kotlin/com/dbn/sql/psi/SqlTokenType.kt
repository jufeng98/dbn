package com.dbn.sql.psi

import com.dbn.sql.SqlLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

/**
 * @author yudong
 */
class SqlTokenType(debugName: @NonNls String) : IElementType(debugName, SqlLanguage.INSTANCE) {
    override fun toString(): String {
        return SqlTokenType::class.java.simpleName + "." + super.toString()
    }
}
