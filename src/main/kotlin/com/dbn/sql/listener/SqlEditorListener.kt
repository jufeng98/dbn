package com.dbn.sql.listener

import com.dbn.browser.DatabaseBrowserManager
import com.dbn.common.util.Editors
import com.dbn.language.editor.ui.DBLanguageFileEditorToolbarForm
import com.dbn.sql.SqlFileType
import com.dbn.vfs.file.DBConsoleVirtualFile
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile

/**
 * @author yudong
 */
class SqlEditorListener : FileEditorManagerListener {
    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        if (file is DBConsoleVirtualFile) {
            return
        }

        if (file.fileType !is SqlFileType) {
            return
        }

        val fileEditor = source.getSelectedEditor(file)!!

        val browserManager = DatabaseBrowserManager.getInstance(source.project)
        browserManager.getFirstConnectionId(source.project) ?: return

        val toolbarForm = DBLanguageFileEditorToolbarForm(fileEditor, source.project, file)
        Editors.addEditorToolbar(fileEditor, toolbarForm)
    }
}
