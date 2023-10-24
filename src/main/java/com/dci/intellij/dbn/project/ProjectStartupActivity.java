package com.dci.intellij.dbn.project;

import com.dci.intellij.dbn.connection.config.ConnectionBundleSettings;
import com.dci.intellij.dbn.debugger.ExecutionConfigManager;
import com.dci.intellij.dbn.plugin.PluginConflictManager;
import com.dci.intellij.dbn.plugin.PluginStatusManager;
import com.dci.intellij.dbn.vfs.DatabaseFileManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class ProjectStartupActivity implements StartupActivity {
    //@Override
    public void runActivity(@NotNull Project project) {
        // make sure dbn connections are loaded
        ConnectionBundleSettings.getInstance(project);

        evaluatePluginStatus(project);
        assesPluginConflict(project);
        removeRunConfigurations(project);
        reopenDatabaseEditors(project);
    }

    private static void evaluatePluginStatus(Project project) {
        PluginStatusManager pluginStatusManager = PluginStatusManager.getInstance();
        pluginStatusManager.evaluatePluginStatus(project);
    }

    private static void assesPluginConflict(Project project) {
        PluginConflictManager conflictManager = PluginConflictManager.getInstance();
        conflictManager.assesPluginConflict(project);
    }

    private static void removeRunConfigurations(Project project) {
        ExecutionConfigManager configManager = ExecutionConfigManager.getInstance(project);
        configManager.removeRunConfigurations();
    }

    private static void reopenDatabaseEditors(Project project) {
        DatabaseFileManager fileManager = DatabaseFileManager.getInstance(project);
        fileManager.reopenDatabaseEditors();
    }
}
