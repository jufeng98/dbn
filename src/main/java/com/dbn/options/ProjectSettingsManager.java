package com.dbn.options;

import com.dbn.options.ui.ProjectSettingsDialog;
import com.dbn.DatabaseNavigator;
import com.dbn.browser.options.DatabaseBrowserSettings;
import com.dbn.code.common.completion.options.CodeCompletionSettings;
import com.dbn.common.action.UserDataKeys;
import com.dbn.common.component.Components;
import com.dbn.common.component.PersistentState;
import com.dbn.common.component.ProjectComponentBase;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.project.Projects;
import com.dbn.common.util.Dialogs;
import com.dbn.common.util.Messages;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.DatabaseType;
import com.dbn.connection.config.ConnectionBundleSettings;
import com.dbn.connection.config.ConnectionConfigListener;
import com.dbn.connection.config.ConnectionConfigType;
import com.dbn.connection.config.tns.TnsImportData;
import com.dbn.connection.operation.options.OperationSettings;
import com.dbn.data.grid.options.DataGridSettings;
import com.dbn.ddl.options.DDLFileSettings;
import com.dbn.editor.data.options.DataEditorSettings;
import com.dbn.execution.common.options.ExecutionEngineSettings;
import com.dbn.navigation.options.NavigationSettings;
import com.dbn.options.general.GeneralProjectSettings;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.dispose.Failsafe.nd;
import static com.dbn.common.message.MessageCallback.when;

@State(
    name = ProjectSettingsManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
@Getter
@Setter
public class ProjectSettingsManager extends ProjectComponentBase implements PersistentState {
    public static final String COMPONENT_NAME = "DBNavigator.Project.Settings";

    private final ProjectSettings projectSettings;
    private ConfigId lastConfigId;

    private ProjectSettingsManager(Project project) {
        super(project, COMPONENT_NAME);
        projectSettings = new ProjectSettings(project);
    }

    public static ProjectSettingsManager getInstance(@NotNull Project project) {
        return Components.projectService(project, ProjectSettingsManager.class);
    }



    public ProjectSettings getProjectSettings() {
        return nd(projectSettings);
    }

    public GeneralProjectSettings getGeneralSettings() {
        return getProjectSettings().getGeneralSettings();
    }

    public DatabaseBrowserSettings getBrowserSettings() {
        return getProjectSettings().getBrowserSettings();
    }

    public NavigationSettings getNavigationSettings() {
        return getProjectSettings().getNavigationSettings();
    }

    public ConnectionBundleSettings getConnectionSettings() {
        return getProjectSettings().getConnectionSettings();
    }

    public DataGridSettings getDataGridSettings() {
        return getProjectSettings().getDataGridSettings();
    }

    public DataEditorSettings getDataEditorSettings() {
        return getProjectSettings().getDataEditorSettings();
    }

    public CodeCompletionSettings getCodeCompletionSettings() {
        return getProjectSettings().getCodeCompletionSettings();
    }

    public OperationSettings getOperationSettings() {
        return getProjectSettings().getOperationSettings();
    }

    public ExecutionEngineSettings getExecutionEngineSettings() {
        return getProjectSettings().getExecutionEngineSettings();
    }

    public DDLFileSettings getDdlFileSettings() {
        return getProjectSettings().getDdlFileSettings();
    }

    public void openDefaultProjectSettings() {
        Dialogs.show(() -> new ProjectSettingsDialog(Projects.getDefaultProject()));
    }

    public void openProjectSettings(ConfigId configId) {
        Dialogs.show(() -> new ProjectSettingsDialog(getProject(), configId));
    }

    public void openConnectionSettings(@Nullable ConnectionId connectionId) {
        Dialogs.show(() -> new ProjectSettingsDialog(getProject(), connectionId));
    }

    public void createConnection(@NotNull DatabaseType databaseType, @NotNull ConnectionConfigType configType) {
        Dialogs.show(() -> new ProjectSettingsDialog(getProject(), databaseType, configType));
    }

    public void createConnections(TnsImportData importData) {
        Dialogs.show(() -> new ProjectSettingsDialog(getProject(), importData));
    }

    /****************************************
     *       PersistentStateComponent       *
     *****************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        projectSettings.writeConfiguration(element);
        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        projectSettings.readConfiguration(element);
        getProject().putUserData(UserDataKeys.PROJECT_SETTINGS_LOADED, true);
    }

    public void exportToDefaultSettings() {
        Project project = getProject();
        Messages.showQuestionDialog(
                project, "Default project settings",
                "This will overwrite your default settings with the ones from the current project (including database connections configuration). \nAre you sure you want to continue?",
                new String[]{"Yes", "No"}, 0,
                option -> when(option == 0, () -> {
                    try {
                        Element element = new Element("state");
                        getProjectSettings().writeConfiguration(element);

                        ConnectionBundleSettings.IS_IMPORT_EXPORT_ACTION.set(true);
                        ProjectSettings defaultProjectSettings = ProjectSettings.getDefault();
                        defaultProjectSettings.readConfiguration(element);
                        Messages.showInfoDialog(project, "Project settings", "Project settings exported as default");
                    } finally {
                        ConnectionBundleSettings.IS_IMPORT_EXPORT_ACTION.set(false);
                    }
                }));
    }

    public void importDefaultSettings(final boolean isNewProject) {
        Project project = getProject();
        Boolean settingsLoaded = project.getUserData(UserDataKeys.PROJECT_SETTINGS_LOADED);
        if (settingsLoaded == null || !settingsLoaded || !isNewProject) {
            String message = isNewProject ?
                    "Do you want to import the default project settings into project \"" + project.getName() + "\"?":
                    "Your current settings will be overwritten with the default project settings, " +
                    "including database connections configuration.\n" +
                    "Are you sure you want to import the default project settings into project \"" + project.getName() + "\"?";
            Messages.showQuestionDialog(
                    project, "Default project settings",
                    message,
                    new String[]{"Yes", "No"}, 0,
                    option -> when(option == 0, () -> {
                        try {
                            Element element = new Element("state");
                            ProjectSettings defaultProjectSettings = ProjectSettings.getDefault();
                            defaultProjectSettings.writeConfiguration(element);

                            ConnectionBundleSettings.IS_IMPORT_EXPORT_ACTION.set(true);
                            getProjectSettings().readConfiguration(element);

                            ProjectEvents.notify(project,
                                    ConnectionConfigListener.TOPIC,
                                    (listener) -> listener.connectionsChanged());

                            if (!isNewProject) {
                                Messages.showInfoDialog(project, "Project settings", "Default project settings loaded to project \"" + project.getName() + "\".");
                            }
                        } finally {
                            ConnectionBundleSettings.IS_IMPORT_EXPORT_ACTION.set(false);
                        }
                    }));
        }
    }
}
