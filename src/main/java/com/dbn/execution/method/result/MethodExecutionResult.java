package com.dbn.execution.method.result;

import com.dbn.common.action.DataKeys;
import com.dbn.common.dispose.DisposableContainers;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.ref.WeakRef;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.jdbc.DBNResultSet;
import com.dbn.data.model.resultSet.ResultSetDataModel;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.ExecutionResultBase;
import com.dbn.execution.common.options.ExecutionEngineSettings;
import com.dbn.execution.method.ArgumentValue;
import com.dbn.execution.method.ArgumentValueHolder;
import com.dbn.execution.method.MethodExecutionContext;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.execution.method.result.ui.MethodExecutionResultForm;
import com.dbn.object.DBArgument;
import com.dbn.object.DBMethod;
import com.dbn.object.DBTypeAttribute;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodExecutionResult extends ExecutionResultBase<MethodExecutionResultForm> {
    private final WeakRef<MethodExecutionInput> executionInput;
    private final List<ArgumentValue> argumentValues = new ArrayList<>();
    private final DBDebuggerType debuggerType;
    private String logOutput;
    private int executionDuration;

    private Map<DBObjectRef<DBArgument>, ResultSetDataModel> cursorModels = DisposableContainers.map(this);

    public MethodExecutionResult(MethodExecutionInput executionInput, DBDebuggerType debuggerType) {
        this.executionInput = WeakRef.of(executionInput);
        this.debuggerType = debuggerType;
    }

    public int getExecutionDuration() {
        return executionDuration;
    }

    public void calculateExecDuration() {
        this.executionDuration = (int) (System.currentTimeMillis() - getExecutionContext().getExecutionTimestamp());
    }

    public void addArgumentValue(DBArgument argument, Object value) throws SQLException {
        ArgumentValueHolder<Object> valueStore = ArgumentValue.createBasicValueHolder(value);
        ArgumentValue argumentValue = new ArgumentValue(argument, valueStore);
        argumentValues.add(argumentValue);
        if (value instanceof DBNResultSet) {
            DBNResultSet resultSet = (DBNResultSet) value;
            if (cursorModels == null) {
                cursorModels = new HashMap<>();
            }

            ExecutionEngineSettings settings = ExecutionEngineSettings.getInstance(argument.getProject());
            int maxRecords = settings.getStatementExecutionSettings().getResultSetFetchBlockSize();
            ResultSetDataModel dataModel = new ResultSetDataModel(resultSet, getConnection(), maxRecords);
            cursorModels.put(DBObjectRef.of(argument), dataModel);
        }
    }

    public void addArgumentValue(DBArgument argument, DBTypeAttribute attribute, Object value) {
        ArgumentValueHolder<Object> valueStore = ArgumentValue.createBasicValueHolder(value);
        ArgumentValue argumentValue = new ArgumentValue(argument, attribute, valueStore);
        argumentValues.add(argumentValue);
    }


    public List<ArgumentValue> getArgumentValues() {
        return argumentValues;
    }

    public ArgumentValue getArgumentValue(DBObjectRef<DBArgument> argumentRef) {
        for (ArgumentValue argumentValue : argumentValues) {
            if (argumentValue.getArgumentRef().equals(argumentRef)) {
                return argumentValue;
            }
        }
        return null;
    }


    @Nullable
    @Override
    public MethodExecutionResultForm createForm() {
        return new MethodExecutionResultForm(this);
    }

    @Override
    @NotNull
    public String getName() {
        return getMethod().getName();
    }

    @Override
    public Icon getIcon() {
        return getMethod().getOriginalIcon();
    }

    @NotNull
    public MethodExecutionInput getExecutionInput() {
        return executionInput.ensure();
    }

    public MethodExecutionContext getExecutionContext() {
        return getExecutionInput().getExecutionContext();
    }

    @NotNull
    public DBMethod getMethod() {
        return Failsafe.nn(getExecutionInput().getMethod());
    }


    @Override
    @NotNull
    public Project getProject() {
        return getMethod().getProject();
    }

    @Override
    public ConnectionId getConnectionId() {
        return getExecutionInput().getConnectionId();
    }

    @Override
    @NotNull
    public ConnectionHandler getConnection() {
        return getMethod().getConnection();
    }

    @Override
    public PsiFile createPreviewFile() {
        return null;
    }

    public boolean hasCursorResults() {
        for (ArgumentValue argumentValue: argumentValues) {
            if (argumentValue.isCursor()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSimpleResults() {
        for (ArgumentValue argumentValue: argumentValues) {
            if (!argumentValue.isCursor()) {
                return true;
            }
        }
        return false;
    }

    public DBDebuggerType getDebuggerType() {
        return debuggerType;
    }

    public ResultSetDataModel getTableModel(DBArgument argument) {
        return cursorModels.get(argument.ref());
    }

    public String getLogOutput() {
        return logOutput;
    }

    public void setLogOutput(String logOutput) {
        this.logOutput = logOutput;
    }

    /********************************************************
     *                    Data Provider                     *
     ********************************************************/
    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        if (DataKeys.METHOD_EXECUTION_RESULT.is(dataId)) {
            return MethodExecutionResult.this;
        }
        return null;
    }
}
