package com.dci.intellij.dbn.options.action;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettingsManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ProjectSettingsOpenAction extends ProjectAction {
    private final ConfigId configId;

    public ProjectSettingsOpenAction(ConfigId configId, boolean showIcon) {
        super("Settings...", null, showIcon ? Icons.ACTION_OPTIONS : null);
        this.configId = configId;
    }

    @Override
    protected final void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(project);

        if (configId == ConfigId.CONNECTIONS) {
            DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
            ConnectionHandler activeConnection = browserManager.getActiveConnection();
            ConnectionId connectionId = activeConnection == null ? null : activeConnection.getConnectionId();
            settingsManager.openConnectionSettings(connectionId);
        }
         else {
            settingsManager.openProjectSettings(configId);
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        //presentation.setIcon(Icons.ACTION_SETTINGS);
        presentation.setText("Settings...");
    }
}
