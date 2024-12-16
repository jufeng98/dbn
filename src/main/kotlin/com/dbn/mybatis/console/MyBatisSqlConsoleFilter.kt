package com.dbn.mybatis.console

import com.intellij.execution.filters.Filter

class MyBatisSqlConsoleFilter : Filter {
    private var sqlLine: String? = null

    override fun applyFilter(line: String, entireLength: Int): Filter.Result? {
        if (line.length > 20000) {
            return null
        }

        val sqlIdx = line.indexOf(PRE_KEY)
        if (sqlIdx != -1) {
            sqlLine = line
            return null
        }

        val paramIdx = line.indexOf(PARAM_KEY)
        if (paramIdx == -1) {
            return null
        }

        if (sqlLine == null) {
            return null
        }

        val startOffset = entireLength - line.length

        val hyperlinkInfo = ExtractSqlHyperlinkInfo(line, sqlLine!!)
        return Filter.Result(
            startOffset + paramIdx, startOffset + paramIdx + PARAM_KEY.length - 2, hyperlinkInfo
        )
    }

    companion object {
        const val PRE_KEY = "Preparing: "
        const val PARAM_KEY = "Parameters: "
    }
}
