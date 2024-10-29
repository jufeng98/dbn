package com.dbn.sql.gutter

import com.dbn.browser.DatabaseBrowserManager
import com.dbn.connection.ConnectionHandler
import com.dbn.connection.SchemaId
import com.dbn.connection.mapping.FileConnectionContextManager
import com.dbn.execution.statement.StatementExecutionManager
import com.dbn.language.common.element.ElementTypeBundle
import com.dbn.language.common.element.impl.NamedElementType
import com.dbn.language.sql.SQLLanguage
import com.dbn.sql.psi.SqlRoot
import com.dbn.vfs.file.MySqlDBConsoleVirtualFile
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.navigation.GotoRelatedItem
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ConstantFunction
import org.jdom.Document
import org.jdom.Element

/**
 * @author yudong
 */
class SqlExecutorLineMarker : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element !is SqlRoot) {
            return
        }

        val file = element.containingFile.virtualFile
        if (file is MySqlDBConsoleVirtualFile) {
            return
        }

        val project = element.project
        val injectionHost = InjectedLanguageManager.getInstance(project).getInjectionHost(element)
        if (injectionHost != null) {
            return
        }

        val browserManager = DatabaseBrowserManager.getInstance(project)
        val connectionId = browserManager.getFirstConnectionId(project) ?: return

        val contextManager = FileConnectionContextManager.getInstance(project)

        runInEdt {
            val connection = ConnectionHandler.get(connectionId)
            contextManager.setConnection(file, connection)

            val dbScheme = browserManager.getFirstConnectionConfigDbScheme(project)
            contextManager.setDatabaseSchema(file, SchemaId.from(dbScheme))
        }

        val fileEditorManager = FileEditorManager.getInstance(project)
        val selectedEditor = fileEditorManager.selectedEditor ?: return

        val lineMarkerInfo = RelatedItemLineMarkerInfo(
            PsiTreeUtil.getDeepestFirst(element),
            element.textRange,
            AllIcons.Actions.Execute,
            ConstantFunction("执行SQL"),
            { _, _ ->
                runInEdt {
                    val document = Document()
                    document.setRootElement(Element("test"))
                    val elementTypeBundle = ElementTypeBundle(SQLLanguage.INSTANCE.mainLanguageDialect, null, document)
                    val namedElementType = NamedElementType(elementTypeBundle, "")

                    val executablePsiElement = MockExecutablePsiElement(element.node, namedElementType, element)
                    val executionManager = StatementExecutionManager.getInstance(project)
                    val executionProcessor =
                        executionManager.getExecutionProcessor(selectedEditor, executablePsiElement, true)

                    executionManager.executeStatement(executionProcessor!!, DataContext.EMPTY_CONTEXT)
                }
            },
            GutterIconRenderer.Alignment.CENTER
        ) {
            GotoRelatedItem.createItems(
                emptyList<PsiElement>()
            )
        }

        result.add(lineMarkerInfo)
    }

}
