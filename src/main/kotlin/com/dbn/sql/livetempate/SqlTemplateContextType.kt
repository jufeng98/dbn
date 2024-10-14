package com.dbn.sql.livetempate

import com.dbn.sql.SqlFileType
import com.dbn.sql.parser.SqlFile
import com.intellij.codeInsight.template.FileTypeBasedContextType
import com.intellij.codeInsight.template.TemplateActionContext

/**
 * @author yudong
 */
open class SqlTemplateContextType protected constructor() :
    FileTypeBasedContextType("gfp-sql", "SQL", SqlFileType.INSTANCE) {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        val psiFile = templateActionContext.file
        return psiFile is SqlFile
    }
}
