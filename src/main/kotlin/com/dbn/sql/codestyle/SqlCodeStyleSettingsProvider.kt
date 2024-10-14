package com.dbn.sql.codestyle

import com.dbn.sql.SqlLanguage
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

/**
 * @author yudong
 */
class SqlCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings,
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, this.configurableDisplayName) {

            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return SqlCodeStyleMainPanel(currentSettings, settings)
            }

        }
    }

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return SqlCodeStyleSettings(settings)
    }

    override fun getConfigurableDisplayName(): String {
        return SqlLanguage.INSTANCE.displayName
    }

    override fun getLanguage(): Language {
        return SqlLanguage.INSTANCE
    }

}
