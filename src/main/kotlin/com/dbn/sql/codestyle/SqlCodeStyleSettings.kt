package com.dbn.sql.codestyle

import com.dbn.sql.SqlLanguage
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

/**
 * @author yudong
 */
class SqlCodeStyleSettings(settings: CodeStyleSettings) : CustomCodeStyleSettings(SqlLanguage.INSTANCE.id, settings) {
    @JvmField
    var spaceBetweenSymbol: Boolean = true

    @JvmField
    var keywordCase = SqlCaseStyle.LOWER.myId

    @JvmField
    var removeAntiQuote: Boolean = true
}
