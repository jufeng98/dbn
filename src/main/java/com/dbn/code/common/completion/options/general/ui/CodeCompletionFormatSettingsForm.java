package com.dbn.code.common.completion.options.general.ui;

import com.dbn.code.common.completion.options.general.CodeCompletionFormatSettings;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CodeCompletionFormatSettingsForm extends ConfigurationEditorForm<CodeCompletionFormatSettings> {
    private JCheckBox enforceCaseCheckBox;
    private JPanel mainPanel;

    public CodeCompletionFormatSettingsForm(CodeCompletionFormatSettings settings) {
        super(settings);
        resetFormChanges();

        registerComponent(mainPanel);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        CodeCompletionFormatSettings settings = getConfiguration();
        settings.setEnforceCodeStyleCase(enforceCaseCheckBox.isSelected());
    }

    @Override
    public void resetFormChanges() {
        CodeCompletionFormatSettings settings = getConfiguration();
        enforceCaseCheckBox.setSelected(settings.isEnforceCodeStyleCase());
    }
}
