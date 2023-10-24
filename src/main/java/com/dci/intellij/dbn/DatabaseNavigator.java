package com.dci.intellij.dbn;

import com.dci.intellij.dbn.common.component.ApplicationComponentBase;
import com.dci.intellij.dbn.common.component.EagerService;
import com.dci.intellij.dbn.common.component.PersistentState;
import com.dci.intellij.dbn.common.file.FileTypeService;
import com.dci.intellij.dbn.common.options.setting.Settings;
import com.dci.intellij.dbn.diagnostics.Diagnostics;
import com.dci.intellij.dbn.plugin.DBNPluginStateListener;
import com.dci.intellij.dbn.plugin.PluginConflictManager;
import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginStateManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.extensions.ExtensionsArea;
import com.intellij.openapi.extensions.PluginId;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.dci.intellij.dbn.common.component.Components.applicationService;

@Slf4j
@State(
    name = DatabaseNavigator.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class DatabaseNavigator extends ApplicationComponentBase implements PersistentState, EagerService {
    public static final String COMPONENT_NAME = "DBNavigator.Application.Settings";
    public static final String STORAGE_FILE = "dbnavigator.xml";

    public static final PluginId SQL_PLUGIN_ID = PluginId.getId("com.intellij.database");
    public static final PluginId DBN_PLUGIN_ID = PluginId.getId("DBN");
    /*static {
        Extensions.getRootArea().
                getExtensionPoint(CodeStyleSettingsProvider.EXTENSION_POINT_NAME).
                registerExtension(new SQLCodeStyleSettingsProvider());
    }*/

    public DatabaseNavigator() {
        super(COMPONENT_NAME);
        PluginStateManager.addStateListener(new DBNPluginStateListener());
        //new NotificationGroup("Database Navigator", NotificationDisplayType.TOOL_WINDOW, true, ExecutionManager.TOOL_WINDOW_ID);

        PluginConflictManager.getInstance();
        FileTypeService.getInstance();

        registerExecutorExtension();

    }

    private static void registerExecutorExtension() {
        try {
            ExtensionsArea extensionArea = ApplicationManager.getApplication().getExtensionArea();
            boolean available = extensionArea.hasExtensionPoint(Executor.EXECUTOR_EXTENSION_NAME);
            if (!available) extensionArea.getExtensionPoint(Executor.EXECUTOR_EXTENSION_NAME).registerExtension(new DefaultDebugExecutor());
        } catch (Throwable e) {
            log.error("Failed to register debug executor extension", e);
        }
    }

    public static DatabaseNavigator getInstance() {
        return applicationService(DatabaseNavigator.class);
    }

    @NotNull
    public static IdeaPluginDescriptor getPluginDescriptor() {
        return Objects.requireNonNull(PluginManager.getPlugin(DBN_PLUGIN_ID));
    }

    public String getName() {
        return null;
    }

    /*********************************************
     *            PersistentStateComponent       *
     *********************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        Element diagnosticsElement = Settings.newElement(element, "diagnostics");
        Diagnostics.writeState(diagnosticsElement);
        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        Element diagnosticsElement = element.getChild("diagnostics");
        Diagnostics.readState(diagnosticsElement);
    }
}

