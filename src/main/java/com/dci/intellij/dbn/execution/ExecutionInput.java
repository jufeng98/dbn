package com.dci.intellij.dbn.execution;

import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.common.options.PersistentConfiguration;
import com.dci.intellij.dbn.common.options.setting.Settings;
import com.dci.intellij.dbn.common.project.ProjectRef;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.SchemaId;
import com.dci.intellij.dbn.connection.context.DatabaseContextBase;
import com.dci.intellij.dbn.execution.common.options.ExecutionEngineSettings;
import com.dci.intellij.dbn.execution.common.options.ExecutionTimeoutSettings;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dci.intellij.dbn.common.util.Unsafe.cast;

@Getter
@Setter
public abstract class ExecutionInput extends StatefulDisposableBase implements StatefulDisposable, DatabaseContextBase, PersistentConfiguration {
    private final ExecutionTimeout executionTimeout;
    private final ExecutionTimeout debugExecutionTimeout;
    private final ExecutionTarget executionTarget;

    private final ProjectRef project;
    private ConnectionRef targetConnection;
    private SchemaId targetSchemaId;

    private final Latent<ExecutionContext> executionContext = Latent.basic(() -> createExecutionContext());

    @NotNull
    public final <C extends ExecutionContext> C getExecutionContext() {
        return cast(executionContext.get());
    }

    protected void resetExecutionContext() {
        executionContext.reset();
    }

    protected abstract ExecutionContext createExecutionContext();

    public ExecutionInput(Project project, ExecutionTarget executionTarget) {
        this.project = ProjectRef.of(project);
        this.executionTarget = executionTarget;
        executionTimeout = new ExecutionTimeout(project, executionTarget, false);
        debugExecutionTimeout = new ExecutionTimeout(project, executionTarget, true);
    }

    @NotNull
    public ExecutionTimeoutSettings getExecutionTimeoutSettings() {
        Project project = getProject();
        ExecutionEngineSettings executionEngineSettings = ExecutionEngineSettings.getInstance(project);
        return executionEngineSettings.getExecutionTimeoutSettings(getExecutionTarget());
    }

    @NotNull
    public final Project getProject() {
        return project.ensure();
    }

    @Nullable
    public final ConnectionHandler getTargetConnection() {
        return ConnectionRef.get(targetConnection);
    }

    public void setTargetConnection(@Nullable ConnectionHandler connection) {
        this.targetConnection = ConnectionRef.of(connection);
    }

    @NotNull
    public <C extends ExecutionContext> C initExecutionContext() {
        ExecutionContext context = getExecutionContext();
        context.reset();
        return cast(context);
    }

    public int getExecutionTimeout() {
        return executionTimeout.get();
    }

    public void setExecutionTimeout(int timeout) {
        executionTimeout.set(timeout);
    }

    public int getDebugExecutionTimeout() {
        return debugExecutionTimeout.get();
    }

    public void setDebugExecutionTimeout(int timeout) {
        debugExecutionTimeout.set(timeout);
    }

    @Override
    public void readConfiguration(Element element) {
        executionTimeout.set(Settings.integerAttribute(element, "execution-timeout", executionTimeout.get()));
        debugExecutionTimeout.set(Settings.integerAttribute(element, "debug-execution-timeout", debugExecutionTimeout.get()));
    }

    @Override
    public void writeConfiguration(Element element) {
        Settings.setIntegerAttribute(element, "execution-timeout", executionTimeout.get());
        Settings.setIntegerAttribute(element, "debug-execution-timeout", debugExecutionTimeout.get());
    }

    @NotNull
    public final ExecutionTarget getExecutionTarget() {
        return executionTarget;
    }

    @Override
    public void disposeInner() {
        nullify();
    }

    public String getDebuggerVersion() {
        ConnectionHandler connection = getConnection();
        return connection == null ? "Unknown" :connection.getDebuggerVersion();
    }
}
