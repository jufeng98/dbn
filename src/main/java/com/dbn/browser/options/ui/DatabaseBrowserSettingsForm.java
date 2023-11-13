package com.dbn.browser.options.ui;

import com.dbn.browser.options.DatabaseBrowserSettings;
import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class DatabaseBrowserSettingsForm extends CompositeConfigurationEditorForm<DatabaseBrowserSettings> {
    private JPanel mainPanel;
    private JPanel generalSettingsPanel;
    private JPanel filterSettingsPanel;
    private JPanel sortingSettingsPanel;
    private JPanel editorSettingsPanel;

    public DatabaseBrowserSettingsForm(DatabaseBrowserSettings settings) {
        super(settings);
        generalSettingsPanel.add(settings.getGeneralSettings().createComponent(), BorderLayout.CENTER);
        filterSettingsPanel.add(settings.getFilterSettings().createComponent(), BorderLayout.CENTER);
        sortingSettingsPanel.add(settings.getSortingSettings().createComponent(), BorderLayout.CENTER);
        editorSettingsPanel.add(settings.getEditorSettings().createComponent(), BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
