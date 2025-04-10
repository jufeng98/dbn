package com.dbn.generator.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.common.thread.Command;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.thread.Progress;
import com.dbn.common.util.Editors;
import com.dbn.common.util.Messages;
import com.dbn.connection.ConnectionAction;
import com.dbn.connection.context.DatabaseContextBase;
import com.dbn.ddl.MessageDialog;
import com.dbn.generator.StatementGeneratorResult;
import com.dbn.language.common.psi.PsiUtil;
import com.dbn.language.sql.SQLFileType;
import com.dbn.utils.NotifyUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;

public abstract class GenerateStatementAction extends ProjectAction implements DatabaseContextBase {
    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ConnectionAction.invoke("generating the statement", false, this,
                action -> Progress.prompt(project, getConnection(), true,
                        "Extracting statement",
                        "Extracting " + e.getPresentation().getText(),
                        progress -> {
                            StatementGeneratorResult result = generateStatement(project);
                            if (result.getMessages().hasErrors()) {
                                Messages.showErrorDialog(project, "Error generating statement", result.getMessages());
                            } else {
                                pasteStatement(result, project);
                            }
                        }));
    }


    private void pasteStatement(StatementGeneratorResult result, Project project) {
        Dispatch.run(() -> {
            Editor editor = Editors.getSelectedEditor(project, SQLFileType.INSTANCE);
            if (editor != null) {
                pasteToEditor(editor, result);
            } else {
                WriteAction.run(() -> {
                    MessageDialog messageDialog = new MessageDialog(project, result.getStatement(),
                            () -> pasteToClipboard(result, project)
                    );
                    messageDialog.show();
                });
            }
        });
    }

    private static void pasteToClipboard(StatementGeneratorResult result, Project project) {
        StringSelection content = new StringSelection(result.getStatement());

        CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
        copyPasteManager.setContents(content);
        NotifyUtil.INSTANCE.notifySuccess(project,"DDL 已复制到剪切板!");
        // Messages.showInfoDialog(project, "Statement extracted", "SQL statement exported to clipboard.");
    }

    private static void pasteToEditor(final Editor editor, final StatementGeneratorResult generatorResult) {
        Command.run(
                editor.getProject(),
                "Extract statement",
                () -> {
                    String statement = generatorResult.getStatement();
                    PsiUtil.moveCaretOutsideExecutable(editor);
                    int offset = EditorModificationUtil.insertStringAtCaret(editor, statement + "\n\n", false, true);
                    offset = offset - statement.length() - 2;
                    /*editor.getMarkupModel().addRangeHighlighter(offset, offset + statement.length(),
                            HighlighterLayer.SELECTION,
                            EditorColorsManager.getInstance().getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES),
                            HighlighterTargetArea.EXACT_RANGE);*/
                    editor.getSelectionModel().setSelection(offset, offset + statement.length());
                    editor.getCaretModel().moveToOffset(offset);

                });
    }

    protected abstract StatementGeneratorResult generateStatement(Project project);
}
