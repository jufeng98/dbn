package com.dbn.debugger.common.config;

import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.statement.StatementExecutionInput;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.psi.ExecutablePsiElement;
import com.dbn.object.DBMethod;
import com.dbn.object.DBSchema;
import com.dbn.object.type.DBObjectType;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public abstract class DBStatementRunConfig extends DBRunConfig<StatementExecutionInput> {
    private StatementExecutionInput executionInput;

    public DBStatementRunConfig(Project project, DBStatementRunConfigFactory factory, String name, DBRunConfigCategory category) {
        super(project, factory, name, category);
    }

    @Override
    public boolean canRun() {
        return super.canRun() && getExecutionInput() != null;
    }

    @Nullable
    @Override
    public DBLanguagePsiFile getSource() {
        if (executionInput == null) return null;
        return executionInput.getExecutionProcessor().getPsiFile();
    }

    @Override
    public List<DBMethod> getMethods() {
        if (executionInput == null) return Collections.emptyList();

        ExecutablePsiElement executablePsiElement = executionInput.getExecutionProcessor().getCachedExecutable();
        if (executablePsiElement == null) return Collections.emptyList();

        List<DBMethod> methods = new ArrayList<>();
        executablePsiElement.collectObjectReferences(DBObjectType.METHOD, object -> {
            if (object instanceof DBMethod) {
                DBMethod method = (DBMethod) object;
                DBSchema schema = method.getSchema();
                if (!schema.isSystemSchema() && !schema.isPublicSchema()) {
                    methods.add(method);
                }
            }
        });
        return methods;

    }

    @Override
    @Nullable
    public String suggestedName() {
        if (getCategory() == DBRunConfigCategory.GENERIC) {
            String defaultRunnerName = getType().getDefaultRunnerName();
            if (getDebuggerType() == DBDebuggerType.JDWP) {
                defaultRunnerName = defaultRunnerName + " (JDWP)";
            }
            return defaultRunnerName;
        }
        return null;
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {

    }

    @Override
    public @Nullable Icon getIcon() {
        Icon defaultIcon = super.getIcon();
        if (getCategory() != DBRunConfigCategory.CUSTOM) return defaultIcon;

        StatementExecutionInput executionInput = getExecutionInput();
        if (executionInput == null) return defaultIcon;

        StatementExecutionProcessor executionProcessor = executionInput.getExecutionProcessor();
        if (executionProcessor == null) return defaultIcon;

        return executionProcessor.getIcon();
    }
}
