package com.dbn.debugger.common.config;

import com.dbn.common.dispose.Disposer;
import com.dbn.debugger.common.config.ui.DBProgramRunConfigurationEditorForm;
import com.dbn.execution.ExecutionInput;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dbn.common.dispose.Checks.isValid;

public abstract class DBRunConfigEditor<T extends DBRunConfig, F extends DBProgramRunConfigurationEditorForm<T>, I extends ExecutionInput> extends SettingsEditor<T> {
    private T configuration;
    private F configurationEditorForm;

    public DBRunConfigEditor(T configuration) {
        this.configuration = configuration;
    }

    public T getConfiguration() {
        return configuration;
    }

    protected abstract F createConfigurationEditorForm();

    public F getConfigurationEditorForm(boolean create) {
        if (create && !isValid(configurationEditorForm)) {
            configurationEditorForm = createConfigurationEditorForm();
        }
        return configurationEditorForm;
    }

    @Override
    protected void disposeEditor() {
        configurationEditorForm = Disposer.replace(configurationEditorForm, null);
    }

    @Override
    protected void resetEditorFrom(@NotNull T configuration) {
        getConfigurationEditorForm(true).readConfiguration(configuration);
    }

    @Override
    protected void applyEditorTo(@NotNull T configuration) throws ConfigurationException {
        getConfigurationEditorForm(true).writeConfiguration(configuration);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        configurationEditorForm = getConfigurationEditorForm(true);
        return configurationEditorForm.getComponent();
    }

    public abstract void setExecutionInput(I executionInput);
}
