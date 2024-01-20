package com.dbn.debugger.common.config;

import com.dbn.common.util.Cloneable;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.DatabaseFeature;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.DatabaseDebuggerManager;
import com.dbn.debugger.options.DebuggerTypeOption;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.execution.method.MethodExecutionManager;
import com.dbn.object.DBMethod;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

import static com.dbn.common.options.setting.Settings.newElement;

public abstract class DBMethodRunConfig extends DBRunConfig<MethodExecutionInput> implements Cloneable<DBMethodRunConfig> {
    private Map<DBObjectRef<DBMethod>, MethodExecutionInput> methodSelectionHistory = new HashMap<>();

    public DBMethodRunConfig(Project project, DBMethodRunConfigFactory factory, String name,DBRunConfigCategory category) {
        super(project, factory, name, category);
    }

    public Collection<MethodExecutionInput> getMethodSelectionHistory() {
        return methodSelectionHistory.values();
    }

    @Override
    public void setExecutionInput(MethodExecutionInput executionInput) {
        MethodExecutionInput input = getExecutionInput();
        if (input != null && !input.equals(executionInput)) {
            methodSelectionHistory.put(input.getMethodRef(), input);
        }
        super.setExecutionInput(executionInput);
    }

    @Override
    public boolean canRun() {
        if (!super.canRun()) return false;
        if (getMethod() == null) return false;

        DebuggerTypeOption debuggerTypeOption = getMethod().getConnection().getSettings().getDebuggerSettings().getDebuggerType().getSelectedOption();
        if (debuggerTypeOption == DebuggerTypeOption.JDWP) {
            return DBDebuggerType.JDWP.isSupported();
        }
        return true;
    }

    @Override
    public MethodExecutionInput getExecutionInput() {
        return super.getExecutionInput();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if (getCategory() == DBRunConfigCategory.CUSTOM) {
            MethodExecutionInput executionInput = getExecutionInput();
            if (executionInput == null) {
                throw new RuntimeConfigurationError("No or invalid method selected. The database connection is down, obsolete or method has been dropped.");
            }

            if (executionInput.isObsolete()) {
                throw new RuntimeConfigurationError(
                        "Method " + executionInput.getMethodRef().getQualifiedName() + " could not be resolved. " +
                                "The database connection is down or method has been dropped.");
            }

            DBMethod method = getMethod();
            if (method == null) return;

            ConnectionHandler connection = method.getConnection();
            if (!DatabaseFeature.DEBUGGING.isSupported(connection)){
                throw new RuntimeConfigurationError(
                        "Debugging is not supported for " + connection.getDatabaseType().getName() +" databases.");
            }

            DebuggerTypeOption debuggerTypeOption = connection.getSettings().getDebuggerSettings().getDebuggerType().getSelectedOption();
            if (debuggerTypeOption == DebuggerTypeOption.JDWP) {
                DatabaseDebuggerManager.checkJdwpConfiguration();
            }
        }

    }

    @Nullable
    @Override
    public DBMethod getSource() {
        return getMethod();
    }

    @Nullable
    public DBMethod getMethod() {
        MethodExecutionInput executionInput = getExecutionInput();
        return executionInput == null ? null : executionInput.getMethod();
    }

    @Override
    public List<DBMethod> getMethods() {
        ArrayList<DBMethod> methods = new ArrayList<>();
        DBMethod method = getMethod();
        if (method != null) {
            methods.add(method);
        }
        return methods;
    }

    @Override
    public void writeExternal(@NotNull Element element) throws WriteExternalException {
        super.writeExternal(element);
        MethodExecutionInput executionInput = getExecutionInput();
        if (executionInput != null && getCategory() == DBRunConfigCategory.CUSTOM) {
            Element methodIdentifierElement = newElement(element, "method-identifier");
            executionInput.getMethodRef().writeState(methodIdentifierElement);

            Element methodIdentifierHistoryElement = newElement(element, "method-identifier-history");
            for (MethodExecutionInput histExecutionInput : methodSelectionHistory.values()) {
                methodIdentifierElement = newElement(methodIdentifierHistoryElement, "method-identifier");
                histExecutionInput.getMethodRef().writeState(methodIdentifierElement);
            }
        }
    }

    @Override
    public void readExternal(@NotNull Element element) throws InvalidDataException {
        super.readExternal(element);
        MethodExecutionManager executionManager = MethodExecutionManager.getInstance(getProject());
        if (getCategory() == DBRunConfigCategory.CUSTOM) {
            Element methodIdentifierElement = element.getChild("method-identifier");
            if (methodIdentifierElement != null) {
                DBObjectRef<DBMethod> methodRef = new DBObjectRef<>();
                methodRef.readState(methodIdentifierElement);

                MethodExecutionInput executionInput = executionManager.getExecutionInput(methodRef);
                setExecutionInput(executionInput);
            }

            Element methodIdentifierHistoryElement = element.getChild("method-identifier-history");
            if (methodIdentifierHistoryElement != null) {
                for (Element child : methodIdentifierHistoryElement.getChildren()) {
                    DBObjectRef<DBMethod> methodRef = new DBObjectRef<>();
                    methodRef.readState(child);

                    MethodExecutionInput executionInput = executionManager.getExecutionInput(methodRef);
                    methodSelectionHistory.put(methodRef, executionInput);
                }
            }
        }
    }

    @Override
    public DBMethodRunConfig clone() {
        DBMethodRunConfig runConfiguration = (DBMethodRunConfig) super.clone();
        MethodExecutionInput executionInput = getExecutionInput();
        runConfiguration.setExecutionInput(executionInput == null ? null : executionInput.clone());
        runConfiguration.methodSelectionHistory = new HashMap<>(methodSelectionHistory);

        return runConfiguration;
    }

    @Override
    public String suggestedName() {
        if (getCategory() == DBRunConfigCategory.CUSTOM) {
            MethodExecutionInput executionInput = getExecutionInput();
            if (executionInput != null) {
                setGeneratedName(true);
                String runnerName = executionInput.getMethodRef().getObjectName();
                if (getDebuggerType() == DBDebuggerType.JDWP) {
                    runnerName = runnerName + " (JDWP)";
                }
                return runnerName;
            }
        } else {
            String defaultRunnerName = getType().getDefaultRunnerName();
            if (getDebuggerType() == DBDebuggerType.JDWP) {
                defaultRunnerName = defaultRunnerName + " (JDWP)";
            }
            return defaultRunnerName;
        }
        return null;
    }

    @Override
    public Icon getIcon() {
        Icon defaultIcon = super.getIcon();
        if (getCategory() != DBRunConfigCategory.CUSTOM) return defaultIcon;

        MethodExecutionInput executionInput = getExecutionInput();
        if (executionInput == null) return defaultIcon;

        DBMethod method = executionInput.getMethod();
        if (method == null) return defaultIcon;

        return method.getIcon();
    }
}
