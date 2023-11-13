package com.dbn.execution.statement.action;

import com.dbn.common.icon.Icons;
import com.dbn.common.util.Documents;
import com.dbn.common.util.Editors;
import com.dbn.execution.ExecutionStatus;
import com.dbn.execution.statement.processor.StatementExecutionCursorProcessor;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.execution.statement.result.StatementExecutionResult;
import com.dbn.execution.statement.result.StatementExecutionStatus;
import com.dbn.execution.statement.StatementExecutionContext;
import com.dbn.execution.statement.StatementExecutionManager;
import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.PsiElementRef;
import com.dbn.language.common.PsiFileRef;
import com.dbn.language.common.psi.ExecutablePsiElement;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.common.dispose.Checks.isNotValid;
import static com.dbn.common.dispose.Checks.isValid;

public class StatementGutterAction extends AnAction {
    private final PsiFileRef<DBLanguagePsiFile> psiFile;
    private final PsiElementRef<ExecutablePsiElement> psiElement;

    public StatementGutterAction(ExecutablePsiElement executablePsiElement) {
        psiFile = PsiFileRef.of(executablePsiElement.getFile());
        psiElement = PsiElementRef.of(executablePsiElement);
    }

    @Nullable
    private DBLanguagePsiFile getPsiFile() {
        return psiFile.get();
    }

    private VirtualFile getVirtualFile() {
        DBLanguagePsiFile psiFile = getPsiFile();
        return psiFile == null ? null : psiFile.getVirtualFile();
    }

    @Nullable
    public ExecutablePsiElement getExecutablePsiElement() {
        ExecutablePsiElement executablePsiElement = PsiElementRef.get(psiElement);
        if (isValid(executablePsiElement)) return executablePsiElement;

        return null;

/*
        return Read.call(this, a -> {
            DBLanguagePsiFile psiFile = a.getPsiFile();
            if (isNotValid(psiFile)) return null;

            PsiElement elementAtOffset = psiFile.findElementAt(a.elementOffset);
            if (elementAtOffset != null && !(elementAtOffset instanceof BasePsiElement)) {
                elementAtOffset = elementAtOffset.getParent();
            }
            if (elementAtOffset instanceof ExecutablePsiElement) {
                return (ExecutablePsiElement) elementAtOffset;
            } else if (elementAtOffset instanceof BasePsiElement) {
                BasePsiElement<?> basePsiElement = (BasePsiElement) elementAtOffset;
                ExecutablePsiElement executablePsiElement = basePsiElement.findEnclosingElement(ExecutablePsiElement.class);

                if (isValid(executablePsiElement)) {
                    return executablePsiElement;
                }
            }
            return null;
        });
*/
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        StatementExecutionProcessor executionProcessor = getExecutionProcessor(false);
        DataContext dataContext = e.getDataContext();

        if (executionProcessor != null && !executionProcessor.isDirty()) {
            Project project = executionProcessor.getProject();
            StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
            StatementExecutionContext context = executionProcessor.getExecutionContext();
            if (context.is(ExecutionStatus.EXECUTING) || context.is(ExecutionStatus.QUEUED)) {
                executionProcessor.cancelExecution();
            } else {
                StatementExecutionResult executionResult = executionProcessor.getExecutionResult();
                if (executionResult == null || !(executionProcessor instanceof StatementExecutionCursorProcessor) || executionProcessor.isDirty()) {
                    executionManager.executeStatement(executionProcessor, dataContext);
                } else {
                    executionProcessor.navigateToResult();
                }
            }
        } else {
            executionProcessor = getExecutionProcessor(true);
            if (executionProcessor != null) {
                Project project = executionProcessor.getProject();
                StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
                executionManager.executeStatement(executionProcessor, dataContext);
            }
        }
    }


    @NotNull
    public Icon getIcon() {
        StatementExecutionProcessor executionProcessor = getExecutionProcessor(false);
        if (executionProcessor == null) return Icons.STMT_EXECUTION_RUN;

        StatementExecutionContext context = executionProcessor.getExecutionContext();
        if (context.is(ExecutionStatus.EXECUTING)) return Icons.STMT_EXECUTION_STOP;
        if (context.is(ExecutionStatus.QUEUED)) return Icons.STMT_EXECUTION_STOP_QUEUED;

        StatementExecutionResult executionResult = executionProcessor.getExecutionResult();
        if (executionResult == null) return Icons.STMT_EXECUTION_RUN;

        StatementExecutionStatus executionStatus = executionResult.getExecutionStatus();
        if (executionStatus == StatementExecutionStatus.SUCCESS){
            if (executionProcessor instanceof StatementExecutionCursorProcessor) {
                return executionProcessor.isDirty() ?
                        Icons.STMT_EXEC_RESULTSET_RERUN :
                        Icons.STMT_EXEC_RESULTSET;
            } else {
                return Icons.STMT_EXECUTION_INFO_RERUN;
            }
        }

        if (executionStatus == StatementExecutionStatus.ERROR) return Icons.STMT_EXECUTION_ERROR_RERUN;
        if (executionStatus == StatementExecutionStatus.WARNING) return Icons.STMT_EXECUTION_WARNING_RERUN;

        return Icons.STMT_EXECUTION_RUN;
    }

    @Nullable
    private StatementExecutionProcessor getExecutionProcessor(boolean create) {
        DBLanguagePsiFile psiFile = getPsiFile();
        if (psiFile == null) return null;

        ExecutablePsiElement executablePsiElement = getExecutablePsiElement();
        if (executablePsiElement == null) return null;

        Project project = psiFile.getProject();
        Document document = Documents.getDocument(psiFile);
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        FileEditor[] selectedEditors = fileEditorManager.getSelectedEditors();
        for (FileEditor fileEditor : selectedEditors) {
            Editor editor = Editors.getEditor(fileEditor);
            if (editor == null) continue;
            if (editor.getDocument() != document) continue;

            StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
            return executionManager.getExecutionProcessor(fileEditor, executablePsiElement, create);
        }
        return null;
    }


    @Nullable
    public String getTooltipText() {
        StatementExecutionProcessor executionProcessor = getExecutionProcessor(false);
        if (isNotValid(executionProcessor)) return null;

        StatementExecutionResult executionResult = executionProcessor.getExecutionResult();
        if (executionResult == null) {
            StatementExecutionContext context = executionProcessor.getExecutionContext();
            if (context.is(ExecutionStatus.EXECUTING)) return "Statement execution is in progress. Cancel?";
            if (context.is(ExecutionStatus.QUEUED)) return "Statement execution is queued. Cancel?";
            
        } else {
            StatementExecutionStatus executionStatus = executionResult.getExecutionStatus();
            if (executionStatus == StatementExecutionStatus.ERROR)   return "Statement executed with errors. Execute again?";
            if (executionStatus == StatementExecutionStatus.WARNING) return "Statement executed with warnings. Execute again?";
            if (executionStatus == StatementExecutionStatus.SUCCESS) return "Statement executed successfully. Execute again?";
        }
        return null;
    }
}
