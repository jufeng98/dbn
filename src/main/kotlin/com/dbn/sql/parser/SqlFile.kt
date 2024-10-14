package com.dbn.sql.parser

import com.dbn.sql.SqlFileType
import com.dbn.sql.SqlLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

/**
 * @author yudong
 */
class SqlFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, SqlLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return SqlFileType.INSTANCE
    }

    override fun toString(): String {
        return "SQL File"
    }
}
