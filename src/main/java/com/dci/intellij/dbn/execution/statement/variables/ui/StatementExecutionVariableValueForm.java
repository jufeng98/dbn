package com.dci.intellij.dbn.execution.statement.variables.ui;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.listener.ComboBoxSelectionKeyListener;
import com.dci.intellij.dbn.common.ui.misc.DBNComboBox;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.data.editor.ui.ListPopupValuesProvider;
import com.dci.intellij.dbn.data.editor.ui.TextFieldPopupType;
import com.dci.intellij.dbn.data.editor.ui.TextFieldWithPopup;
import com.dci.intellij.dbn.data.type.GenericDataType;
import com.dci.intellij.dbn.execution.statement.StatementExecutionManager;
import com.dci.intellij.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dci.intellij.dbn.execution.statement.variables.StatementExecutionVariable;
import com.dci.intellij.dbn.execution.statement.variables.StatementExecutionVariables;
import com.dci.intellij.dbn.execution.statement.variables.VariableValueProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.UIUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.dci.intellij.dbn.common.ui.util.TextFields.onTextChange;


public class StatementExecutionVariableValueForm extends DBNFormBase {
    private JPanel mainPanel;
    private JLabel variableNameLabel;
    private JPanel valueFieldPanel;
    private JLabel errorLabel;
    private JPanel dataTypePanel;

    private final DBNComboBox<GenericDataType> dataTypeComboBox;

    @Getter
    private final StatementExecutionVariable variable;
    private final TextFieldWithPopup<?> editorComponent;

    StatementExecutionVariableValueForm(StatementExecutionInputForm parent, final StatementExecutionVariable variable) {
        super(parent);
        this.variable = variable;
        errorLabel.setVisible(false);
        errorLabel.setIcon(Icons.STMT_EXECUTION_ERROR);

        variableNameLabel.setText(variable.getName());
        variableNameLabel.setIcon(Icons.DBO_VARIABLE);

        dataTypeComboBox = new DBNComboBox<>(
                GenericDataType.LITERAL,
                GenericDataType.NUMERIC,
                GenericDataType.DATE_TIME);
        dataTypeComboBox.setSelectedValue(variable.getDataType());
        dataTypePanel.add(dataTypeComboBox, BorderLayout.CENTER);

        StatementExecutionProcessor executionProcessor = parent.getExecutionProcessor();
        Project project = executionProcessor.getProject();
        StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
        StatementExecutionVariables variablesCache = executionManager.getExecutionVariables();

        editorComponent = new TextFieldWithPopup<>(project);
        editorComponent.createCalendarPopup(false);
        editorComponent.createValuesListPopup(new ListPopupValuesProvider() {
            @Override
            public String getDescription() {
                return "History Values List";
            }

            @Override
            public List<String> getValues() {
                List<String> values = new ArrayList<>();
                VirtualFile virtualFile = executionProcessor.getVirtualFile();
                Set<StatementExecutionVariable> variables = variablesCache.getVariables(virtualFile);
                for (StatementExecutionVariable executionVariable : variables) {
                    if (Objects.equals(executionVariable.getName(), variable.getName())) {
                        Iterable<String> valueHistory = executionVariable.getValueHistory();
                        for (String value : valueHistory) {
                            values.add(value);
                        }
                    }
                }

                return values;
            }
        }, true);
        editorComponent.setPopupEnabled(TextFieldPopupType.CALENDAR, variable.getDataType() == GenericDataType.DATE_TIME);
        valueFieldPanel.add(editorComponent, BorderLayout.CENTER);
        JTextField textField = editorComponent.getTextField();
        String value = variable.getValue();
        if (Strings.isEmpty(value)) {
            VirtualFile virtualFile = executionProcessor.getVirtualFile();
            StatementExecutionVariable cachedVariable = variablesCache.getVariable(virtualFile, variable.getName());
            if (cachedVariable != null) {
                textField.setForeground(UIUtil.getLabelDisabledForeground());
                textField.setText(cachedVariable.getValue());

                onTextChange(textField, e -> textField.setForeground(UIUtil.getTextFieldForeground()));
                dataTypeComboBox.setSelectedValue(cachedVariable.getDataType());
            }
        } else {
            textField.setText(value);
        }


        textField.addKeyListener(ComboBoxSelectionKeyListener.create(dataTypeComboBox, false));

        variable.setPreviewValueProvider(new VariableValueProvider() {
            @Override
            public String getValue() {
                return textField.getText().trim();
            }

            @Override
            public GenericDataType getDataType() {
                return dataTypeComboBox.getSelectedValue();
            }

        });

        dataTypeComboBox.addListener((oldValue, newValue) -> {
            variable.setDataType(newValue);
            editorComponent.setPopupEnabled(TextFieldPopupType.CALENDAR, newValue == GenericDataType.DATE_TIME);
            getParentForm().updatePreview();
        });

        textField.setToolTipText("<html>While editing variable value, press <b>Up/Down</b> keys to change data type");

        Disposer.register(this, editorComponent);
    }

    public StatementExecutionInputForm getParentForm() {
        return ensureParentComponent();
    }

    void showErrorLabel(String errorText) {
        errorLabel.setVisible(true);
        errorLabel.setText(errorText);
    }
    
    void hideErrorLabel(){
        errorLabel.setVisible(false);
        errorLabel.setText(null);
    }

    void saveValue() {
        String trim = editorComponent.getTextField().getText().trim();
        variable.setValue(trim);
        variable.setDataType(dataTypeComboBox.getSelectedValue());
        StatementExecutionProcessor executionProcessor = getParentForm().getExecutionProcessor();
        Project project = executionProcessor.getProject();
        StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
        executionManager.cacheVariable(executionProcessor.getVirtualFile(), variable);
    }

    protected int[] getMetrics(int[] metrics) {
        return new int[] {
            Math.max(metrics[0], (int) variableNameLabel.getPreferredSize().getWidth()),
            Math.max(metrics[1], (int) valueFieldPanel.getPreferredSize().getWidth())};
    }

    protected void adjustMetrics(int[] metrics) {
        variableNameLabel.setPreferredSize(new Dimension(metrics[0], variableNameLabel.getHeight()));
        valueFieldPanel.setPreferredSize(new Dimension(metrics[1], valueFieldPanel.getHeight()));
    }


    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public JTextField getEditorComponent() {
        return editorComponent.getTextField();
    }

    @Override
    public void disposeInner() {
        variable.setPreviewValueProvider(null);
        super.disposeInner();
    }
}
