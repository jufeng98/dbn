package com.dbn.debugger.common.config;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.context.DatabaseContext;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.ExecutionInput;
import com.dbn.object.DBMethod;
import com.intellij.execution.ExecutionTarget;
import com.intellij.execution.configurations.LocatableConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.dbn.common.options.setting.Settings.getEnum;
import static com.dbn.common.options.setting.Settings.setEnum;

@Getter
@Setter
public abstract class DBRunConfig<I extends ExecutionInput> extends RunConfigurationBase implements RunConfigurationWithSuppressedDefaultRunAction, LocatableConfiguration {
    private boolean generatedName = true;
    private DBRunConfigCategory category;
    private DBDebuggerType debuggerType = DBDebuggerType.JDBC;
    private I executionInput;

    protected DBRunConfig(Project project, DBRunConfigFactory factory, String name, DBRunConfigCategory category) {
        super(project, factory, name);
        this.category = category;
        this.debuggerType = factory == null ? this.debuggerType : factory.getDebuggerType();
    }

    public boolean canRun() {
        return category == DBRunConfigCategory.CUSTOM;
    }

    @NotNull
    @Override
    public DBRunConfigType getType() {
        return (DBRunConfigType) super.getType();
    }

    @Override
    public boolean canRunOn(@NotNull ExecutionTarget target) {
        return canRun();
    }

    @Override
    public void writeExternal(@NotNull Element element) throws WriteExternalException {
        super.writeExternal(element);
        setEnum(element, "category", category);
        setEnum(element, "debugger-type", debuggerType);
    }

    @Override
    public void readExternal(@NotNull Element element) throws InvalidDataException {
        super.readExternal(element);
        category = getEnum(element, "category", category);
        debuggerType = getEnum(element, "debugger-type", debuggerType);
    }

    public abstract List<DBMethod> getMethods();

    @Nullable
    public abstract DatabaseContext getDatabaseContext();

    @Nullable
    public final ConnectionHandler getConnection() {
        DatabaseContext databaseContext = getDatabaseContext();
        return databaseContext == null ? null : databaseContext.getConnection();
    }


    @Override
    public boolean excludeCompileBeforeLaunchOption() {
        return true;
    }
}
