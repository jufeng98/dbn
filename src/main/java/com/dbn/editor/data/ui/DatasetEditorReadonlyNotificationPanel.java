package com.dbn.editor.data.ui;

import com.dbn.common.environment.EnvironmentManager;
import com.dbn.common.message.MessageType;
import com.dbn.common.util.Messages;
import com.dbn.editor.DBContentType;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.options.ConfigId;
import com.dbn.options.ProjectSettingsManager;
import com.intellij.openapi.project.Project;

import static com.dbn.common.message.MessageCallback.when;

public class DatasetEditorReadonlyNotificationPanel extends DatasetEditorNotificationPanel{
    public DatasetEditorReadonlyNotificationPanel(final DBSchemaObject schemaObject) {
        super(isReadonly(schemaObject) ? MessageType.NEUTRAL : MessageType.WARNING);
        String environmentName = schemaObject.getEnvironmentType().getName();
        final Project project = schemaObject.getProject();

        if (isReadonly(schemaObject)) {
            setText("READONLY DATA - This is meant to prevent accidental data changes in \"" + environmentName + "\" environments (check environment settings)");
            createActionLabel("Edit Mode", () -> Messages.showQuestionDialog(project,
                    "Enable edit-mode",
                    "Are you sure you want to enable editing for " + schemaObject.getQualifiedNameWithType(),
                    new String[]{"Yes", "Cancel"}, 0,
                    option -> when(option == 0, () -> {
                        EnvironmentManager environmentManager = EnvironmentManager.getInstance(project);
                        environmentManager.enableEditing(schemaObject, DBContentType.DATA);
                    })));
        } else {
            setText("EDITABLE DATA! - Edit-mode enabled (the environment \"" + environmentName + "\" is configured as \"Readonly Data\")");
            createActionLabel("Cancel Editing", () -> {
                EnvironmentManager environmentManager = EnvironmentManager.getInstance(project);
                environmentManager.disableEditing(schemaObject, DBContentType.DATA);
            });
        }

        createActionLabel("Settings", () -> {
            ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(project);
            settingsManager.openProjectSettings(ConfigId.GENERAL);
        });
    }

    private static boolean isReadonly(DBSchemaObject schemaObject) {
        Project project = schemaObject.getProject();
        EnvironmentManager environmentManager = EnvironmentManager.getInstance(project);
        return environmentManager.isReadonly(schemaObject, DBContentType.DATA);
    }
}
