package com.dbn.sql.codestyle

import com.dbn.sql.SqlLanguage
import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizableOptions
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.IndentOptions
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

/**
 * @author yudong
 */
class SqlLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage(): Language {
        return SqlLanguage.INSTANCE
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return SQL_SAMPLE
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor()
    }

    @Suppress("DialogTitleCapitalization")
    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showCustomOption(
                SqlCodeStyleSettings::class.java,
                "spaceBetweenSymbol",
                "附加空格",
                CodeStyleSettingsCustomizableOptions.getInstance().SPACES_OTHER
            )
        } else if (settingsType == SettingsType.LANGUAGE_SPECIFIC) {
            val textList = SqlCaseStyle.values().map { it.presentableText }.toTypedArray()
            val ids = SqlCaseStyle.values().map { it.myId }.toIntArray()
            consumer.showCustomOption(
                SqlCodeStyleSettings::class.java,
                "keywordCase",
                "关键字",
                SqlCaseCodeStyleSpacesPanel.WORD_CASE,
                textList,
                ids
            )

            consumer.showCustomOption(
                SqlCodeStyleSettings::class.java,
                "removeAntiQuote",
                "去掉反引号",
                SqlCaseCodeStyleSpacesPanel.ANTI_QUOTATION_MARK,
            )
        }
    }

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: IndentOptions,
    ) {
        indentOptions.INDENT_SIZE = 4
        // strip all blank lines by default
        commonSettings.KEEP_BLANK_LINES_IN_CODE = 0
    }

}

val SQL_SAMPLE = """
    select 
        `mu.id`,
        mu.name,
        mu.age,
        mv.size
    from
        mall_user mu
    left join mall_video mv on mv.user_id = mu.id
    where 
        id = 1
        and name = 'yu' 
    order by mu.id, mu.name
""".trimIndent()