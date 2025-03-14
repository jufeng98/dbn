package com.dbn.code.common.intention;

import com.dbn.common.icon.Icons;
import com.dbn.common.ref.WeakRef;
import com.dbn.common.util.Editors;
import com.dbn.execution.statement.StatementExecutionManager;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.psi.ExecutablePsiElement;
import com.dbn.language.common.psi.PsiUtil;
import com.intellij.codeInsight.intention.HighPriorityAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class JumpToExecutionResultIntentionAction extends GenericIntentionAction implements HighPriorityAction {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private WeakRef<StatementExecutionProcessor> cachedExecutionProcessor;

    @Override
    @NotNull
    public String getText() {
        return "Navigate to result";
    }


    @Override
    public Icon getIcon(int flags) {
/*        if (cachedExecutionProcessor != null) {
            StatementExecutionProcessor executionProcessor = cachedExecutionProcessor.get();
            if (executionProcessor != null) {
                StatementExecutionResult executionResult = executionProcessor.getExecutionResult();
                if (executionResult != null) {
                    StatementExecutionStatus executionStatus = executionResult.getExecutionStatus();
                    if (executionStatus == StatementExecutionStatus.SUCCESS){
                        if (executionProcessor instanceof StatementExecutionCursorProcessor) {
                            return Icons.STMT_EXEC_RESULTSET;
                        } else {
                            return Icons.COMMON_INFO;
                        }
                    } else if (executionStatus == StatementExecutionStatus.ERROR){
                        return Icons.COMMON_ERROR;
                    } else if (executionStatus == StatementExecutionStatus.WARNING){
                        return Icons.COMMON_WARNING;
                    }
                }
            }
        }*/
        return Icons.STMT_EXECUTION_NAVIGATE;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        if (psiFile instanceof DBLanguagePsiFile) {
            ExecutablePsiElement executable = PsiUtil.lookupExecutableAtCaret(editor, true);
            FileEditor fileEditor = Editors.getFileEditor(editor);
            if (executable != null && fileEditor != null) {
                StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
                StatementExecutionProcessor executionProcessor = executionManager.getExecutionProcessor(fileEditor, executable, false);
                if (executionProcessor != null && executionProcessor.getExecutionResult() != null) {
                    cachedExecutionProcessor = WeakRef.of(executionProcessor);
                    return true;
                }
            }
        }
        cachedExecutionProcessor = null;
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        ExecutablePsiElement executable = PsiUtil.lookupExecutableAtCaret(editor, true);
        FileEditor fileEditor = Editors.getFileEditor(editor);
        if (executable != null && fileEditor != null) {
            StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
            StatementExecutionProcessor executionProcessor = executionManager.getExecutionProcessor(fileEditor, executable, false);
            if (executionProcessor != null) {
                executionProcessor.navigateToResult();
            }
        }
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Override
    protected Integer getGroupPriority() {
        return super.getGroupPriority();
    }
}