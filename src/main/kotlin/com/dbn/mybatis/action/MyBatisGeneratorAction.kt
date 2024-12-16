package com.dbn.mybatis.action

import com.dbn.common.action.ProjectAction
import com.dbn.common.icon.Icons
import com.dbn.mybatis.ui.MyBatisGeneratorForm
import com.dbn.`object`.DBTable
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.project.Project

class MyBatisGeneratorAction(private val dbTable: DBTable) : ProjectAction() {

    override fun update(e: AnActionEvent, project: Project) {
        val presentation: Presentation = e.presentation
        presentation.text = "MyBatis Generator"
        presentation.icon = Icons.EXEC_CONFIG
    }

    override fun actionPerformed(e: AnActionEvent, project: Project) {
        val form = MyBatisGeneratorForm(project, dbTable)
        form.show()
    }
}