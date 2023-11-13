package com.dbn.ddl.options.ui;

import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dbn.ddl.options.DDLFileSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class DDFileSettingsForm extends CompositeConfigurationEditorForm<DDLFileSettings> {
    private JPanel mainPanel;
    private JPanel extensionSettingsPanel;
    private JPanel generalSettingsPanel;

    public DDFileSettingsForm(DDLFileSettings settings) {
        super(settings);
        extensionSettingsPanel.add(settings.getExtensionSettings().createComponent(), BorderLayout.CENTER);
        generalSettingsPanel.add(settings.getGeneralSettings().createComponent(), BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
