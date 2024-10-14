package com.dbn.sql.codestyle

import com.dbn.sql.SqlLanguage
import com.intellij.application.options.codeStyle.WrappingAndBracesPanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

/**
 * @author yudong
 */
class SqlCaseCodeStyleSpacesPanel(settings: CodeStyleSettings?) : WrappingAndBracesPanel(settings) {

    override fun getTabTitle(): String {
        return "大小写"
    }

    override fun customizeSettings() {
        val provider = LanguageCodeStyleSettingsProvider.forLanguage(SqlLanguage.INSTANCE)
        provider?.customizeSettings(this, settingsType)
    }

    override fun getSettingsType(): LanguageCodeStyleSettingsProvider.SettingsType {
        return LanguageCodeStyleSettingsProvider.SettingsType.LANGUAGE_SPECIFIC
    }

    override fun getDefaultLanguage(): Language {
        return SqlLanguage.INSTANCE
    }


    companion object {
        const val WORD_CASE = "单词大小写"
        const val ANTI_QUOTATION_MARK = "反引号"
    }
}
