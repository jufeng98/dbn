package com.dbn.execution.statement.action;

import com.dbn.common.action.BasicAction;
import com.dbn.common.icon.Icons;
import com.dbn.execution.ExecutionStatus;
import com.dbn.execution.statement.StatementExecutionContext;
import com.dbn.execution.statement.StatementExecutionManager;
import com.dbn.execution.statement.processor.StatementExecutionCursorProcessor;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.execution.statement.result.StatementExecutionResult;
import com.dbn.execution.statement.result.StatementExecutionStatus;
import com.dbn.language.common.element.ElementTypeBundle;
import com.dbn.language.common.element.impl.NamedElementType;
import com.dbn.language.sql.SQLLanguage;
import com.dbn.sql.gutter.MockExecutablePsiElement;
import com.dbn.sql.psi.SqlRoot;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import lombok.Getter;
import org.jdom.Document;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.common.dispose.Checks.isNotValid;

/**
 * @author yudong
 */
@Getter
public class MySqlStatementGutterAction extends BasicAction {
    private final SqlRoot psiElement;

    public MySqlStatementGutterAction(SqlRoot psiElement) {
        this.psiElement = psiElement;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        StatementExecutionProcessor executionProcessor = getExecutionProcessor(false, psiElement);
        DataContext dataContext = e.getDataContext();

        if (executionProcessor != null && !executionProcessor.isDirty()) {
            Project project = executionProcessor.getProject();
            StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
            StatementExecutionContext context = executionProcessor.getExecutionContext();
            if (context.is(ExecutionStatus.EXECUTING) || context.is(ExecutionStatus.QUEUED)) {
                executionProcessor.cancelExecution();
            } else {
                StatementExecutionResult executionResult = executionProcessor.getExecutionResult();
                if (executionResult == null
                        || !(executionProcessor instanceof StatementExecutionCursorProcessor)
                        || executionProcessor.isDirty()) {
                    executionManager.executeStatement(executionProcessor, dataContext);
                } else {
                    executionProcessor.navigateToResult();
                }
            }
        } else {
            executionProcessor = getExecutionProcessor(true, psiElement);
            if (executionProcessor != null) {
                Project project = executionProcessor.getProject();
                StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
                executionManager.executeStatement(executionProcessor, dataContext);
            }
        }
    }


    @NotNull
    public Icon getIcon() {
        StatementExecutionProcessor executionProcessor = getExecutionProcessor(false, psiElement);
        if (executionProcessor == null) return Icons.STMT_EXECUTION_RUN;

        StatementExecutionContext context = executionProcessor.getExecutionContext();
        if (context.is(ExecutionStatus.EXECUTING)) return Icons.STMT_EXECUTION_STOP;
        if (context.is(ExecutionStatus.QUEUED)) return Icons.STMT_EXECUTION_STOP_QUEUED;

        StatementExecutionResult executionResult = executionProcessor.getExecutionResult();
        if (executionResult == null) return Icons.STMT_EXECUTION_RUN;

        StatementExecutionStatus executionStatus = executionResult.getExecutionStatus();
        if (executionStatus == StatementExecutionStatus.SUCCESS) {
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
    public static StatementExecutionProcessor getExecutionProcessor(boolean create, SqlRoot psiElement) {
        Project project = psiElement.getProject();
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        FileEditor selectedEditor = fileEditorManager.getSelectedEditor();
        if (selectedEditor == null) {
            return null;
        }

        Document document = new org.jdom.Document();
        document.setRootElement(new Element("test"));
        ElementTypeBundle elementTypeBundle = new ElementTypeBundle(SQLLanguage.INSTANCE.getMainLanguageDialect(),
                null, document);
        NamedElementType namedElementType = new NamedElementType(elementTypeBundle, "");

        MockExecutablePsiElement executablePsiElement = new MockExecutablePsiElement(psiElement.getNode(),
                namedElementType, psiElement, null);
        StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
        return executionManager.getExecutionProcessor(selectedEditor, executablePsiElement, create);
    }

    @Nullable
    public static StatementExecutionProcessor getExecutionProcessor(boolean create, PsiElement psiElement, String sql) {
        Project project = psiElement.getProject();
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        FileEditor selectedEditor = fileEditorManager.getSelectedEditor();
        if (selectedEditor == null) {
            return null;
        }

        Document document = new org.jdom.Document();
        document.setRootElement(new Element("test"));
        ElementTypeBundle elementTypeBundle = new ElementTypeBundle(SQLLanguage.INSTANCE.getMainLanguageDialect(),
                null, document);
        NamedElementType namedElementType = new NamedElementType(elementTypeBundle, "");

        MockExecutablePsiElement executablePsiElement = new MockExecutablePsiElement(psiElement.getNode(),
                namedElementType, psiElement, sql);
        StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
        return executionManager.getExecutionProcessor(selectedEditor, executablePsiElement, create);
    }


    @Nullable
    public String getTooltipText() {
        StatementExecutionProcessor executionProcessor = getExecutionProcessor(false, psiElement);
        if (isNotValid(executionProcessor)) return null;

        StatementExecutionResult executionResult = executionProcessor.getExecutionResult();
        if (executionResult == null) {
            StatementExecutionContext context = executionProcessor.getExecutionContext();
            if (context.is(ExecutionStatus.EXECUTING)) return "Statement execution is in progress. Cancel?";
            if (context.is(ExecutionStatus.QUEUED)) return "Statement execution is queued. Cancel?";

        } else {
            StatementExecutionStatus executionStatus = executionResult.getExecutionStatus();
            if (executionStatus == StatementExecutionStatus.ERROR)
                return "Statement executed with errors. Execute again?";
            if (executionStatus == StatementExecutionStatus.WARNING)
                return "Statement executed with warnings. Execute again?";
            if (executionStatus == StatementExecutionStatus.SUCCESS)
                return "Statement executed successfully. Execute again?";
        }
        return null;
    }
}
