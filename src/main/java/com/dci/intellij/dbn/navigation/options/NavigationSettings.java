package com.dci.intellij.dbn.navigation.options;

import com.dci.intellij.dbn.common.options.CompositeProjectConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.navigation.options.ui.NavigationSettingsForm;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettings;
import com.dci.intellij.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class NavigationSettings extends CompositeProjectConfiguration<ProjectSettings, NavigationSettingsForm> implements TopLevelConfig {
    private final ObjectsLookupSettings objectsLookupSettings = new ObjectsLookupSettings(this);

    public NavigationSettings(ProjectSettings parent) {
        super(parent);
    }

    public static NavigationSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getNavigationSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.NavigationSettings";
    }

    @Override
    public String getDisplayName() {
        return "Navigation";
    }

    @Override
    public String getHelpTopic() {
        return "navigationSettings";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.NAVIGATION;
    }

    @NotNull
    @Override
    public NavigationSettings getOriginalSettings() {
        return getInstance(getProject());
    }


    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @NotNull
    @Override
    public NavigationSettingsForm createConfigurationEditor() {
        return new NavigationSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "navigation-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {objectsLookupSettings};
    }
}
