package com.dbn.options.ui;

import com.dbn.browser.options.DatabaseBrowserSettings;
import com.dbn.code.common.completion.options.CodeCompletionSettings;
import com.dbn.common.options.BasicConfiguration;
import com.dbn.common.options.Configuration;
import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.config.ConnectionBundleSettings;
import com.dbn.connection.config.ui.ConnectionBundleSettingsForm;
import com.dbn.connection.operation.options.OperationSettings;
import com.dbn.data.grid.options.DataGridSettings;
import com.dbn.ddl.options.DDLFileSettings;
import com.dbn.editor.code.options.CodeEditorSettings;
import com.dbn.editor.data.options.DataEditorSettings;
import com.dbn.execution.common.options.ExecutionEngineSettings;
import com.dbn.navigation.options.NavigationSettings;
import com.dbn.options.ConfigId;
import com.dbn.options.ProjectSettings;
import com.dbn.options.general.GeneralProjectSettings;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.JBTabsFactory;
import com.intellij.ui.tabs.TabInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ProjectSettingsForm extends CompositeConfigurationEditorForm<ProjectSettings> {
    private JPanel mainPanel;
    private JPanel tabsPanel;
    private final JBTabs configurationTabs;

    public ProjectSettingsForm(ProjectSettings globalSettings) {
        super(globalSettings);

        configurationTabs = JBTabsFactory.createTabs(globalSettings.ensureProject(), globalSettings);

        tabsPanel.add((Component) configurationTabs, BorderLayout.CENTER);

        ConnectionBundleSettings connectionSettings = globalSettings.getConnectionSettings();
        DatabaseBrowserSettings browserSettings = globalSettings.getBrowserSettings();
        NavigationSettings navigationSettings = globalSettings.getNavigationSettings();
        CodeEditorSettings codeEditorSettings = globalSettings.getCodeEditorSettings();
        CodeCompletionSettings codeCompletionSettings = globalSettings.getCodeCompletionSettings();
        DataGridSettings dataGridSettings = globalSettings.getDataGridSettings();
        DataEditorSettings dataEditorSettings = globalSettings.getDataEditorSettings();
        ExecutionEngineSettings executionEngineSettings = globalSettings.getExecutionEngineSettings();
        OperationSettings operationSettings = globalSettings.getOperationSettings();
        DDLFileSettings ddlFileSettings = globalSettings.getDdlFileSettings();
        GeneralProjectSettings generalSettings = globalSettings.getGeneralSettings();

        addSettingsPanel(connectionSettings);
        addSettingsPanel(browserSettings);
        addSettingsPanel(navigationSettings);
        addSettingsPanel(codeEditorSettings);
        addSettingsPanel(codeCompletionSettings);
        addSettingsPanel(dataGridSettings);
        addSettingsPanel(dataEditorSettings);
        addSettingsPanel(executionEngineSettings);
        addSettingsPanel(operationSettings);
        addSettingsPanel(ddlFileSettings);
        addSettingsPanel(generalSettings);
        globalSettings.reset();

        tabsPanel.setFocusable(true);
    }

    private void addSettingsPanel(BasicConfiguration<?, ?> configuration) {
        JComponent component = configuration.createComponent();
        JBScrollPane scrollPane = new JBScrollPane(component);
        TabInfo tabInfo = new TabInfo(scrollPane);
        tabInfo.setText(configuration.getDisplayName());
        tabInfo.setObject(configuration.getSettingsEditor());
        //tabInfo.setTabColor(GUIUtil.getWindowColor());
        configurationTabs.addTab(tabInfo);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    void selectConnectionSettings(@Nullable ConnectionId connectionId) {
        ConnectionBundleSettings connectionSettings = getConfiguration().getConnectionSettings();
        ConnectionBundleSettingsForm settingsEditor = connectionSettings.getSettingsEditor();
        if (settingsEditor != null) {
            settingsEditor.selectConnection(connectionId);
            selectSettingsEditor(ConfigId.CONNECTIONS);
        }
    }

    void selectSettingsEditor(ConfigId configId) {
        Configuration<?, ?> configuration = getConfiguration().getConfiguration(configId);
        if (configuration != null) {
            ConfigurationEditorForm<?> settingsEditor = configuration.getSettingsEditor();
            if (settingsEditor != null) {
                JComponent component = settingsEditor.getComponent();
                TabInfo tabInfo = getTabInfo(component);
                if (tabInfo != null) {
                    configurationTabs.select(tabInfo, true);
                }
            }
        }
    }

    private TabInfo getTabInfo(JComponent component) {
        for (TabInfo tabInfo : configurationTabs.getTabs()) {
            JBScrollPane scrollPane = (JBScrollPane) tabInfo.getComponent();
            if (scrollPane.getViewport().getView() == component) {
                return tabInfo;
            }
        }
        return null;
    }

    @NotNull
    public Configuration<?, ?> getActiveSettings() {
        TabInfo tabInfo = configurationTabs.getSelectedInfo();
        if (tabInfo == null) {
            return getConfiguration();
        }

        ConfigurationEditorForm<?> configurationEditorForm = (ConfigurationEditorForm<?>) tabInfo.getObject();
        if (configurationEditorForm == null) {
            return getConfiguration();
        }

        return configurationEditorForm.getConfiguration();
    }
}
