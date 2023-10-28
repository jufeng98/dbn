package com.dci.intellij.dbn.ddl.options;

import com.dci.intellij.dbn.common.options.CompositeProjectConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.ddl.options.ui.DDFileSettingsForm;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettings;
import com.dci.intellij.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class DDLFileSettings extends CompositeProjectConfiguration<ProjectSettings, DDFileSettingsForm> implements TopLevelConfig {
    private final DDLFileExtensionSettings extensionSettings = new DDLFileExtensionSettings(this);
    private final DDLFileGeneralSettings generalSettings = new DDLFileGeneralSettings(this);

    public DDLFileSettings(ProjectSettings parent) {
        super(parent);
    }

    public static DDLFileSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getDdlFileSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.DDLFileSettings";
    }

    @Override
    public String getDisplayName() {
        return "DDL Files";
    }

    @Override
    public String getHelpTopic() {
        return "ddlFileSettings";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.DDL_FILES;
    }

    @NotNull
    @Override
    public DDLFileSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /********************************************************
    *                     Configuration                     *
    *********************************************************/
    @Override
    @NotNull
    public DDFileSettingsForm createConfigurationEditor() {
        return new DDFileSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "ddl-file-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                extensionSettings,
                generalSettings};
    }
}
