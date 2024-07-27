package com.dbn.project;

import com.dbn.cache.MetadataCacheService;
import com.dbn.common.compatibility.Compatibility;
import com.dbn.connection.config.ConnectionBundleSettings;
import com.dbn.debugger.ExecutionConfigManager;
import com.dbn.object.impl.DBObjectLoaders;
import com.dbn.plugin.PluginConflictManager;
import com.dbn.plugin.PluginStatusManager;
import com.dbn.vfs.DatabaseFileManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

@Compatibility
public class ProjectStartupActivity implements StartupActivity/*, ProjectActivity*/ {
    //@Override
    public void runActivity(@NotNull Project project) {
        // make sure dbn connections are loaded
        ConnectionBundleSettings.getInstance(project);

        evaluatePluginStatus(project);
        assesPluginConflict(project);
        removeRunConfigurations(project);
        reopenDatabaseEditors(project);
        initMetadata(project);
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

    private void initMetadata(@NotNull Project project) {
        DBObjectLoaders.initLoaders();

        MetadataCacheService cacheService = MetadataCacheService.getService(project);
        // 从本地缓存中初始化数据库元数据信息
        cacheService.initCacheDbTable(project);
    }

/*
    @Override
    public @Nullable Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        return null;
    }
*/
}
