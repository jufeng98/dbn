package com.dbn.utils

import com.intellij.codeInsight.hint.HintManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project

object TooltipUtils {

    fun showTooltip(msg: String, project: Project) {
        val textEditor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        HintManager.getInstance().showInformationHint(textEditor, msg)
    }

}
