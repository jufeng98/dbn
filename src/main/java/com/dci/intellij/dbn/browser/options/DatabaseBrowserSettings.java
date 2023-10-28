package com.dci.intellij.dbn.browser.options;

import com.dci.intellij.dbn.browser.options.ui.DatabaseBrowserSettingsForm;
import com.dci.intellij.dbn.common.options.CompositeProjectConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettings;
import com.dci.intellij.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class DatabaseBrowserSettings
        extends CompositeProjectConfiguration<ProjectSettings, DatabaseBrowserSettingsForm>
        implements TopLevelConfig {

    private final DatabaseBrowserGeneralSettings generalSettings = new DatabaseBrowserGeneralSettings(this);
    private final DatabaseBrowserFilterSettings filterSettings   = new DatabaseBrowserFilterSettings(this);
    private final DatabaseBrowserSortingSettings sortingSettings = new DatabaseBrowserSortingSettings(this);
    private final DatabaseBrowserEditorSettings editorSettings   = new DatabaseBrowserEditorSettings(this);

    public DatabaseBrowserSettings(ProjectSettings parent) {
        super(parent);
    }

    @NotNull
    @Override
    public DatabaseBrowserSettingsForm createConfigurationEditor() {
        return new DatabaseBrowserSettingsForm(this);
    }

    public static DatabaseBrowserSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getBrowserSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.DatabaseBrowserSettings";
    }

    @Override
    public String getDisplayName() {
        return "Database Browser";
    }

    @Override
    public String getHelpTopic() {
        return "browserSettings";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.BROWSER;
    }

    @NotNull
    @Override
    public DatabaseBrowserSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                generalSettings,
                filterSettings,
                sortingSettings,
                editorSettings};
    }

    @Override
    public String getConfigElementName() {
        return "browser-settings";
    }
}
