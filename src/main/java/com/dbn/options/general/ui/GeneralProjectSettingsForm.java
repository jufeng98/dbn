package com.dbn.options.general.ui;

import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dbn.options.general.GeneralProjectSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GeneralProjectSettingsForm extends CompositeConfigurationEditorForm<GeneralProjectSettings> {
    private JPanel mainPanel;
    private JPanel localeSettingsPanel;
    private JPanel environmentSettingsPanel;

    public GeneralProjectSettingsForm(GeneralProjectSettings generalSettings) {
        super(generalSettings);
        resetFormChanges();

        registerComponent(mainPanel);

        localeSettingsPanel.add(generalSettings.getRegionalSettings().createComponent(), BorderLayout.CENTER);
        environmentSettingsPanel.add(generalSettings.getEnvironmentSettings().createComponent(), BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

}
