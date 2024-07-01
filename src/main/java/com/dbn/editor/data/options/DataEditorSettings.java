package com.dbn.editor.data.options;

import com.dbn.common.options.CompositeProjectConfiguration;
import com.dbn.common.options.Configuration;
import com.dbn.editor.data.options.ui.DataEditorSettingsForm;
import com.dbn.options.ConfigId;
import com.dbn.options.ProjectSettings;
import com.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = false)
public class DataEditorSettings extends CompositeProjectConfiguration<ProjectSettings, DataEditorSettingsForm> implements TopLevelConfig {
    private final @Getter(lazy = true) DataEditorPopupSettings popupSettings                       = new DataEditorPopupSettings(this);
    private final @Getter(lazy = true) DataEditorValueListPopupSettings valueListPopupSettings     = new DataEditorValueListPopupSettings(this);
    private final @Getter(lazy = true) DataEditorFilterSettings filterSettings                     = new DataEditorFilterSettings(this);
    private final @Getter(lazy = true) DataEditorGeneralSettings generalSettings                   = new DataEditorGeneralSettings(this);
    private final @Getter(lazy = true) DataEditorQualifiedEditorSettings qualifiedEditorSettings   = new DataEditorQualifiedEditorSettings(this);
    private final @Getter(lazy = true) DataEditorRecordNavigationSettings recordNavigationSettings = new DataEditorRecordNavigationSettings(this);

    public DataEditorSettings(ProjectSettings parent) {
        super(parent);
    }

    public static DataEditorSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getDataEditorSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.DataEditorSettings";
    }

    @Override
    public String getDisplayName() {
        return nls("cfg.dataEditor.title.DataEditor");
    }

    @Override
    public String getHelpTopic() {
        return "dataEditor";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.DATA_EDITOR;
    }

    @NotNull
    @Override
    public DataEditorSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @Override
    @NotNull
    public DataEditorSettingsForm createConfigurationEditor() {
        return new DataEditorSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "dataset-editor-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                getPopupSettings(),
                getValueListPopupSettings(),
                getGeneralSettings(),
                getFilterSettings(),
                getQualifiedEditorSettings(),
                getRecordNavigationSettings()};
    }
}
