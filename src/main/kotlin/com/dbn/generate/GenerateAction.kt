package com.dbn.generate

import com.dbn.generator.ui.GenerateForm
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class GenerateAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        GenerateForm(e.project).show()
    }

}
