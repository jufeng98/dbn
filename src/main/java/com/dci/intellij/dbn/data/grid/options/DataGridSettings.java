package com.dci.intellij.dbn.data.grid.options;

import com.dci.intellij.dbn.common.options.CompositeProjectConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.data.grid.options.ui.DataGridSettingsForm;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettings;
import com.dci.intellij.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class DataGridSettings extends CompositeProjectConfiguration<ProjectSettings, DataGridSettingsForm> implements TopLevelConfig {
    private final DataGridGeneralSettings generalSettings = new DataGridGeneralSettings(this);
    private final DataGridSortingSettings sortingSettings = new DataGridSortingSettings(this);
    private final DataGridAuditColumnSettings auditColumnSettings = new DataGridAuditColumnSettings(this);

    public DataGridSettings(ProjectSettings parent) {
        super(parent);
    }

    public static DataGridSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getDataGridSettings();
    }

    public static boolean isAuditColumn(Project project, String name) {
        DataGridSettings dataGridSettings = getInstance(project);
        return dataGridSettings.getAuditColumnSettings().isAuditColumn(name);
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.DataGridSettings";
    }

    @Override
    public String getDisplayName() {
        return "Data Grid";
    }

    @Override
    public String getHelpTopic() {
        return "dataGrid";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.DATA_GRID;
    }

    @NotNull
    @Override
    public DataGridSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @Override
    @NotNull
    public DataGridSettingsForm createConfigurationEditor() {
        return new DataGridSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "dataset-grid-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                generalSettings,
                sortingSettings,
                auditColumnSettings
        };
    }
}
