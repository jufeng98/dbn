package com.dbn.code.common.completion.options.ui;

import com.dbn.code.common.completion.options.CodeCompletionSettings;
import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class CodeCompletionSettingsForm extends CompositeConfigurationEditorForm<CodeCompletionSettings> {
    private JPanel mainPanel;
    private JPanel filterPanel;
    private JPanel sortingPanel;
    private JPanel formatPanel;

    public CodeCompletionSettingsForm(CodeCompletionSettings codeCompletionSettings) {
        super(codeCompletionSettings);

        filterPanel.add(codeCompletionSettings.getFilterSettings().createComponent(), BorderLayout.CENTER);
        sortingPanel.add(codeCompletionSettings.getSortingSettings().createComponent(), BorderLayout.CENTER);
        formatPanel.add(codeCompletionSettings.getFormatSettings().createComponent(), BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
