package com.dbn.sql.formatter

import com.dbn.sql.codestyle.SqlCodeStyleSettings
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider

/**
 * @author yudong
 */
class SqlFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val customSettings = settings.getCustomSettings(SqlCodeStyleSettings::class.java)
        val containingFile = formattingContext.containingFile

        val sqlBlock = SqlBlock(settings, customSettings, formattingContext.psiElement)

        return FormattingModelProvider.createFormattingModelForPsiFile(containingFile, sqlBlock, settings)
    }

}
