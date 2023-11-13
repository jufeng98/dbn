package com.dbn.options.general;

import com.dbn.common.environment.options.EnvironmentSettings;
import com.dbn.common.locale.options.RegionalSettings;
import com.dbn.common.options.CompositeProjectConfiguration;
import com.dbn.common.options.Configuration;
import com.dbn.options.ConfigId;
import com.dbn.options.TopLevelConfig;
import com.dbn.options.ProjectSettings;
import com.dbn.options.general.ui.GeneralProjectSettingsForm;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class GeneralProjectSettings extends CompositeProjectConfiguration<ProjectSettings, GeneralProjectSettingsForm> implements TopLevelConfig {
    private final RegionalSettings regionalSettings       = new RegionalSettings(this);
    private final EnvironmentSettings environmentSettings = new EnvironmentSettings(this);

    public GeneralProjectSettings(ProjectSettings parent) {
        super(parent);
    }

    public static GeneralProjectSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getGeneralSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.GeneralSettings";
    }

    @Override
    public String getDisplayName() {
        return "General";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.GENERAL;
    }

    @NotNull
    @Override
    public GeneralProjectSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /*********************************************************
     *                      Configuration                    *
     *********************************************************/
    @Override
    @NotNull
    public GeneralProjectSettingsForm createConfigurationEditor() {
        return new GeneralProjectSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "general-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {regionalSettings, environmentSettings};
    }

}
