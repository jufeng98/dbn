package com.dci.intellij.dbn.options.ui;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.browser.options.DatabaseBrowserSettings;
import com.dci.intellij.dbn.code.common.completion.options.CodeCompletionSettings;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.common.thread.BackgroundTask;
import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.dci.intellij.dbn.common.thread.TaskInstructions;
import com.dci.intellij.dbn.common.ui.tab.TabbedPane;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.config.ConnectionBundleSettings;
import com.dci.intellij.dbn.connection.config.ui.ConnectionBundleSettingsForm;
import com.dci.intellij.dbn.connection.operation.options.OperationSettings;
import com.dci.intellij.dbn.data.grid.options.DataGridSettings;
import com.dci.intellij.dbn.ddl.options.DDLFileSettings;
import com.dci.intellij.dbn.editor.code.options.CodeEditorSettings;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.execution.common.options.ExecutionEngineSettings;
import com.dci.intellij.dbn.navigation.options.NavigationSettings;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettings;
import com.dci.intellij.dbn.options.general.GeneralProjectSettings;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginInstaller;
import com.intellij.ide.plugins.PluginManagerMain;
import com.intellij.ide.plugins.PluginNode;
import com.intellij.ide.plugins.RepositoryHelper;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.HyperlinkAdapter;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.PlatformColors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectSettingsEditorForm extends CompositeConfigurationEditorForm<ProjectSettings> {
    private JPanel mainPanel;
    private JPanel tabsPanel;
    private JLabel newVersionLabel;
    private JPanel pluginUpdatePanel;
    private JPanel pluginUpdateLinkPanel;
    private TabbedPane configurationTabs;

    private ProjectSettingsDialog dialog;

    public ProjectSettingsEditorForm(ProjectSettings globalSettings) {
        super(globalSettings);
        globalSettings.reset();

        configurationTabs = new TabbedPane(this);
        //configurationTabs.setAdjustBorders(false);

        tabsPanel.add(configurationTabs, BorderLayout.CENTER);

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
        final GeneralProjectSettings generalSettings = globalSettings.getGeneralSettings();

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
        tabsPanel.setFocusable(true);

        newVersionLabel.setForeground(JBColor.DARK_GRAY);
        newVersionLabel.setIcon(Icons.COMMON_INFO);
        DatabaseNavigator databaseNavigator = DatabaseNavigator.getInstance();
        String pluginVersion = databaseNavigator.getPluginVersion();
        String repositoryPluginVersion = databaseNavigator.getRepositoryPluginVersion();
        if (StringUtil.isNotEmpty(pluginVersion) && StringUtil.isNotEmpty(repositoryPluginVersion)&& repositoryPluginVersion.compareTo(pluginVersion) > 0) {
            Color panelBackground = EditorColorsManager.getInstance().getGlobalScheme().getColor(EditorColors.NOTIFICATION_BACKGROUND);
            newVersionLabel.setText("A new version of the plugin is available (" + repositoryPluginVersion + ")");
            HyperlinkLabel label = new HyperlinkLabel("Update", PlatformColors.BLUE, panelBackground, PlatformColors.BLUE);
            label.addHyperlinkListener(new HyperlinkAdapter() {
                @Override
                protected void hyperlinkActivated(HyperlinkEvent e) {

                    if (dialog != null) dialog.doCancelAction();

                    Project project = generalSettings.getProject();
                    BackgroundTask.invoke(project,
                            TaskInstructions.create("Updating plugin"),
                            (task, progress) -> {
                                try {
                                    List<PluginNode> updateDescriptors = new ArrayList<>();
                                    List<IdeaPluginDescriptor> descriptors = RepositoryHelper.loadCachedPlugins();
                                    List<PluginId> pluginIds = new ArrayList<>();
                                    if (descriptors != null) {
                                        for (IdeaPluginDescriptor descriptor : descriptors) {
                                            pluginIds.add(descriptor.getPluginId());
                                            if (descriptor.getPluginId().toString().equals(DatabaseNavigator.DBN_PLUGIN_ID)) {
                                                PluginNode pluginNode = new PluginNode(descriptor.getPluginId());
                                                pluginNode.setName(descriptor.getName());
                                                pluginNode.setSize("-1");
                                                pluginNode.setRepositoryName(PluginInstaller.UNKNOWN_HOST_MARKER);
                                                updateDescriptors.add(pluginNode);
                                            }
                                        }
                                    }

                                    SimpleLaterInvocator.invoke(() -> {
                                        try {
                                            PluginManagerMain.downloadPlugins(updateDescriptors, descriptors, () -> PluginManagerMain.notifyPluginsUpdated(project), null);
                                        } catch (IOException e1) {
                                            sendErrorNotification("Update Error", "Error updating DBN plugin: " + e1.getMessage());
                                        }
                                    });
                                } catch (Exception ex) {
                                    sendErrorNotification("Update Error", "Error updating DBN plugin: " + ex.getMessage());
                                }

                            });
                }
            });
            pluginUpdateLinkPanel.add(label, BorderLayout.WEST);
            pluginUpdateLinkPanel.setBackground(panelBackground);
            pluginUpdatePanel.setBackground(panelBackground);
        } else {
            pluginUpdatePanel.setVisible(false);
        }

        Disposer.register(this, configurationTabs);
    }

    public void setDialog(ProjectSettingsDialog dialog) {
        this.dialog = dialog;
    }

    private void addSettingsPanel(Configuration configuration) {
        JComponent component = configuration.createComponent();
        JBScrollPane scrollPane = new JBScrollPane(component);
        TabInfo tabInfo = new TabInfo(scrollPane);
        tabInfo.setText(configuration.getDisplayName());
        tabInfo.setObject(configuration);
        //tabInfo.setTabColor(GUIUtil.getWindowColor());
        configurationTabs.addTab(tabInfo);
    }

    public JComponent getComponent() {
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
        Configuration configuration = getConfiguration().getConfiguration(configId);
        if (configuration != null) {
            ConfigurationEditorForm settingsEditor = configuration.getSettingsEditor();
            if (settingsEditor != null) {
                JComponent component = settingsEditor.getComponent();
                if (component != null) {
                    TabInfo tabInfo = getTabInfo(component);
                    if (tabInfo != null) {
                        configurationTabs.select(tabInfo, true);
                    }
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
    public Configuration getActiveSettings() {
        TabInfo tabInfo = configurationTabs.getSelectedInfo();
        if (tabInfo != null) {
            return (Configuration) tabInfo.getObject();
        }
        return getConfiguration();
    }

    public void dispose() {
        dialog = null;
        super.dispose();
    }
}
