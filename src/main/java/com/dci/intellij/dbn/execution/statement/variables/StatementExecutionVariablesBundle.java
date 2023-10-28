package com.dci.intellij.dbn.execution.statement.variables;

import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.locale.Formatter;
import com.dci.intellij.dbn.common.util.Lists;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.data.type.GenericDataType;
import com.dci.intellij.dbn.database.interfaces.DatabaseMetadataInterface;
import com.dci.intellij.dbn.execution.statement.StatementExecutionManager;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.IdentifierCategory;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.psi.ExecVariablePsiElement;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.language.common.psi.lookup.ObjectLookupAdapter;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.type.DBObjectType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.util.*;

import static com.dci.intellij.dbn.common.util.Commons.nvl;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;
import static com.dci.intellij.dbn.execution.statement.variables.VariableNames.adjust;

@Getter
public class StatementExecutionVariablesBundle extends StatefulDisposableBase implements StatefulDisposable {
    public static final Comparator<StatementExecutionVariable> NAME_COMPARATOR = Comparator.comparing(StatementExecutionVariable::getName);
    public static final Comparator<StatementExecutionVariable> OFFSET_COMPARATOR = Comparator.comparingInt(StatementExecutionVariable::getOffset);
    public static final Comparator<StatementExecutionVariable> NAME_LENGTH_COMPARATOR = (o1, o2) -> o2.getName().length() - o1.getName().length();

    private Map<String, String> errors;
    private List<StatementExecutionVariable> variables = new ArrayList<>();

    public StatementExecutionVariablesBundle(List<ExecVariablePsiElement> variablePsiElements) {
        initialize(variablePsiElements);
    }

    public void initialize(List<ExecVariablePsiElement> variablePsiElements) {
        List<StatementExecutionVariable> variables = new ArrayList<>();
        for (ExecVariablePsiElement variablePsiElement : variablePsiElements) {
            String variableName = variablePsiElement.getText();
            StatementExecutionVariable variable = getVariable(variableName);
            if (variable == null) {
                variable = new StatementExecutionVariable(variablePsiElement);
            } else {
                variable.setOffset(variablePsiElement.getTextOffset());
            }

            if (variable.getDataType() == null) {
                DBDataType dataType = lookupDataType(variablePsiElement);
                if (dataType != null && dataType.isNative()) {
                    variable.setDataType(dataType.getGenericDataType());
                } else {
                    GenericDataType variableDataType = cachedVariableDataType(variablePsiElement);
                    variable.setDataType(nvl(variableDataType, GenericDataType.LITERAL));
                }
            }
            uniqueAddVariable(variables, variable);
        }
        this.variables = variables;
    }

    public void cacheVariableDataTypes(ConnectionHandler connection) {
        StatementExecutionManager executionManager = StatementExecutionManager.getInstance(connection.getProject());
        StatementExecutionVariableTypes variableTypes = executionManager.getExecutionVariableTypes();
        for (StatementExecutionVariable variable : variables) {
            variableTypes.setVariableDataType(
                    connection.getConnectionId(),
                    variable.getName(),
                    variable.getDataType());
        }

    }

    @Nullable
    private GenericDataType cachedVariableDataType(ExecVariablePsiElement variablePsiElement) {
        String variableName = variablePsiElement.getText();
        ConnectionId connectionId = variablePsiElement.getConnectionId();
        if (connectionId == null) return null;

        StatementExecutionManager executionManager = StatementExecutionManager.getInstance(variablePsiElement.getProject());
        StatementExecutionVariableTypes executionVariableTypes = executionManager.getExecutionVariableTypes();
        return executionVariableTypes.getVariableDataType(connectionId, variableName);
    }

    private void uniqueAddVariable(List<StatementExecutionVariable> variables, StatementExecutionVariable variable) {
        if (Lists.noneMatch(variables, v -> Objects.equals(v.getName(), variable.getName()))) {
            variables.add(variable);
        }
    }

    public boolean isProvided() {
        for (StatementExecutionVariable variable : variables) {
            if (!variable.isProvided()) {
                return false;
            }
        }
        return true;
    }

    public void populate(Map<String, StatementExecutionVariable> variableCache, boolean force) {
        for (StatementExecutionVariable variable : variables) {
            if (!variable.isProvided() || force) {
                StatementExecutionVariable cacheVariable = variableCache.get(variable.getName());
                if (cacheVariable != null) {
                    variable.populate(cacheVariable);
                }
            }
        }
    }

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    private static DBDataType lookupDataType(ExecVariablePsiElement variablePsiElement) {
        BasePsiElement conditionPsiElement = variablePsiElement.findEnclosingElement(ElementTypeAttribute.CONDITION);
        if (conditionPsiElement == null) return null;

        ObjectLookupAdapter lookupAdapter = new ObjectLookupAdapter(variablePsiElement, IdentifierCategory.REFERENCE, DBObjectType.COLUMN);
        BasePsiElement basePsiElement = lookupAdapter.findInScope(conditionPsiElement);
        if (basePsiElement instanceof IdentifierPsiElement) {
            IdentifierPsiElement columnPsiElement = (IdentifierPsiElement) basePsiElement;
            DBObject object = columnPsiElement.getUnderlyingObject();
            if (object instanceof DBColumn) {
                DBColumn column = (DBColumn) object;
                return column.getDataType();
            }
        }
        return null;
    }

    @Nullable
    public StatementExecutionVariable getVariable(String name) {
        name = adjust(name);
        for (StatementExecutionVariable variable : variables) {
            if (Objects.equals(variable.getName(), name)) {
                return variable;
            }
        }
        return null;
    }

    public String prepareStatementText(@NotNull ConnectionHandler connection, String statementText, boolean forPreview) {
        errors = null;
        List<StatementExecutionVariable> variables = new ArrayList<>(this.variables);
        variables.sort(NAME_LENGTH_COMPARATOR);
        Formatter formatter = Formatter.getInstance(connection.getProject());
        for (StatementExecutionVariable variable : variables) {
            VariableValueProvider previewValueProvider = variable.getPreviewValueProvider();

            String name = ":" + variable.getName();
            String value = forPreview ? previewValueProvider.getValue() : variable.getValue();

            if (Strings.isEmpty(value)) {
                statementText = Strings.replaceIgnoreCase(statementText, name, "NULL /*" + variable.getName() + "*/");
            } else {

                if (!Strings.isEmpty(value)) {
                    GenericDataType genericDataType = forPreview ? previewValueProvider.getDataType() : variable.getDataType();
                    if (genericDataType == GenericDataType.LITERAL) {
                        value = Strings.replace(value, "'", "''");
                        value = '\'' + value + '\'';
                    } else {
                        if (genericDataType == GenericDataType.DATE_TIME){
                            DatabaseMetadataInterface dmi = connection.getMetadataInterface();
                            try {
                                Date date = formatter.parseDateTime(value);
                                value = dmi.createDateString(date);
                            } catch (ParseException e) {
                                conditionallyLog(e);
                                try {
                                    Date date = formatter.parseDate(value);
                                    value = dmi.createDateString(date);
                                } catch (ParseException e1) {
                                    conditionallyLog(e1);
                                    addError(variable, "Invalid date");
                                }
                            }
                        } else if (genericDataType == GenericDataType.NUMERIC){
                            try {
                                formatter.parseNumber(value);
                            } catch (ParseException e) {
                                conditionallyLog(e);
                                addError(variable, "Invalid number");
                            }

                        } else {
                            throw new IllegalArgumentException("Data type " + genericDataType.getName() + " not supported with execution variables.");
                        }
                    }

                    statementText = Strings.replaceIgnoreCase(statementText, name, value + " /*" + name + "*/");
                }
            }
        }
        return statementText;
    }

    private void addError(StatementExecutionVariable variable, String value) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        errors.put(variable.getName(), value);
    }

    public String getError(StatementExecutionVariable variable) {
        return errors == null ? null : errors.get(variable.getName());
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
