package com.dci.intellij.dbn.debugger.jdwp.config;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.debugger.DBDebuggerType;
import com.dci.intellij.dbn.debugger.common.config.DBRunConfigCategory;
import com.dci.intellij.dbn.debugger.common.config.DBStatementRunConfig;
import com.dci.intellij.dbn.debugger.common.config.DBStatementRunConfigFactory;
import com.dci.intellij.dbn.debugger.common.config.DBStatementRunConfigType;
import com.dci.intellij.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dci.intellij.dbn.common.dispose.Failsafe.nd;

public class DBStatementJdwpRunConfigFactory extends DBStatementRunConfigFactory<DBStatementRunConfigType, DBStatementRunConfig> {
    public DBStatementJdwpRunConfigFactory(@NotNull DBStatementRunConfigType type) {
        super(type, DBDebuggerType.JDWP);
    }

    @Override
    public DBStatementJdwpRunConfig createConfiguration(Project project, String name, DBRunConfigCategory category) {
        return new DBStatementJdwpRunConfig(project, this, name, category);
    }

    @Override
    public Icon getIcon(@NotNull RunConfiguration configuration) {
        return Icons.EXEC_STATEMENT_CONFIG;
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new DBStatementJdwpRunConfig(project, this, "", DBRunConfigCategory.TEMPLATE);
    }

    @NotNull
    @Override
    public String getName() {
        return super.getName() + " (JDWP)";
    }

    @Override
    public DBStatementJdwpRunConfig createConfiguration(@NotNull StatementExecutionProcessor executionProcessor) {
        Project project = executionProcessor.getProject();
        VirtualFile virtualFile = nd(executionProcessor.getVirtualFile());
        String runnerName = virtualFile.getName();

        DBStatementJdwpRunConfig configuration = createConfiguration(project, runnerName, DBRunConfigCategory.CUSTOM);
        configuration.setExecutionInput(executionProcessor.getExecutionInput());
        return configuration;
    }
}
