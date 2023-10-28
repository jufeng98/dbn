package com.dci.intellij.dbn.editor.code.options;

import com.dci.intellij.dbn.common.options.CompositeProjectConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.editor.code.options.ui.CodeEditorSettingsForm;
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
public class CodeEditorSettings extends CompositeProjectConfiguration<ProjectSettings, CodeEditorSettingsForm> implements TopLevelConfig {
    private final CodeEditorGeneralSettings generalSettings           = new CodeEditorGeneralSettings(this);
    private final CodeEditorConfirmationSettings confirmationSettings = new CodeEditorConfirmationSettings(this);

    public CodeEditorSettings(ProjectSettings parent) {
        super(parent);
    }

    public static CodeEditorSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getCodeEditorSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.CodeEditorSettings";
    }

    @Override
    public String getDisplayName() {
        return "Code Editor";
    }

    @Override
    public String getHelpTopic() {
        return "codeEditor";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.CODE_EDITOR;
    }

    @NotNull
    @Override
    public CodeEditorSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @Override
    @NotNull
    public CodeEditorSettingsForm createConfigurationEditor() {
        return new CodeEditorSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "code-editor-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
            generalSettings,
            confirmationSettings};
    }
}
