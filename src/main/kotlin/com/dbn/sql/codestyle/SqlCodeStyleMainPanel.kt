package com.dbn.sql.codestyle

import com.dbn.sql.SqlLanguage
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * @author yudong
 */
class SqlCodeStyleMainPanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) :
    TabbedLanguageCodeStylePanel(SqlLanguage.INSTANCE, currentSettings, settings) {
    override fun initTabs(settings: CodeStyleSettings?) {
        addTab(SqlCaseCodeStyleSpacesPanel(settings))
        addIndentOptionsTab(settings)
        addSpacesTab(settings)
        addBlankLinesTab(settings)
        addWrappingAndBracesTab(settings)
    }

}
