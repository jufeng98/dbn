package com.dbn.code.common.style.options.ui;

import com.dbn.code.common.style.options.ProjectCodeStyleSettings;
import com.dbn.common.icon.Icons;
import com.dbn.common.options.Configuration;
import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.JBTabsFactory;
import com.intellij.ui.tabs.TabInfo;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class CodeStyleSettingsForm extends CompositeConfigurationEditorForm<ProjectCodeStyleSettings> {
    private JPanel mainPanel;
    private final JBTabs languageTabs;

    public CodeStyleSettingsForm(ProjectCodeStyleSettings settings) {
        super(settings);
        languageTabs = JBTabsFactory.createTabs(getProject(), settings);
        //languageTabs.setAdjustBorders(false);
        mainPanel.add((Component) languageTabs, BorderLayout.CENTER);
        addSettingsPanel(settings.getSQLCodeStyleSettings(), Icons.FILE_SQL);
        addSettingsPanel(settings.getPSQLCodeStyleSettings(), Icons.FILE_PLSQL);
    }

    private void addSettingsPanel(Configuration<?, ?> configuration, Icon icon) {
        JComponent component = configuration.createComponent();
        //noinspection DataFlowIssue
        TabInfo tabInfo = new TabInfo(component);
        tabInfo.setText(configuration.getDisplayName());
        tabInfo.setObject(configuration);
        tabInfo.setIcon(icon);
        languageTabs.addTab(tabInfo);
    }


    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
