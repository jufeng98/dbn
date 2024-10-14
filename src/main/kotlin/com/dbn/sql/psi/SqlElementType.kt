package com.dbn.sql.psi

import com.dbn.sql.SqlLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

/**
 * @author yudong
 */
class SqlElementType(debugName: @NonNls String) : IElementType(debugName, SqlLanguage.INSTANCE)
