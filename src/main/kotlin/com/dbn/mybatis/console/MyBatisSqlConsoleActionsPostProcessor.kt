package com.dbn.mybatis.console

import com.intellij.execution.filters.ConsoleFilterProvider
import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project

class MyBatisSqlConsoleActionsPostProcessor : ConsoleFilterProvider {

    override fun getDefaultFilters(project: Project): Array<Filter> {
        return arrayOf(MyBatisSqlConsoleFilter())
    }

}
