package com.dbn.editor.code.options.ui;

import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dbn.editor.code.options.CodeEditorSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class CodeEditorSettingsForm extends CompositeConfigurationEditorForm<CodeEditorSettings> {
    private JPanel mainPanel;
    private JPanel generalSettingsPanel;
    private JPanel confirmationSettingsPanel;

    public CodeEditorSettingsForm(CodeEditorSettings settings) {
        super(settings);
        generalSettingsPanel.add(settings.getGeneralSettings().createComponent(), BorderLayout.CENTER);
        confirmationSettingsPanel.add(settings.getConfirmationSettings().createComponent(), BorderLayout.CENTER);
        resetFormChanges();
    }


    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
