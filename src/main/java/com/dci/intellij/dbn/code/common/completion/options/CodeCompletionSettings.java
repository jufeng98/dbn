package com.dci.intellij.dbn.code.common.completion.options;

import com.dci.intellij.dbn.code.common.completion.options.filter.CodeCompletionFiltersSettings;
import com.dci.intellij.dbn.code.common.completion.options.general.CodeCompletionFormatSettings;
import com.dci.intellij.dbn.code.common.completion.options.sorting.CodeCompletionSortingSettings;
import com.dci.intellij.dbn.code.common.completion.options.ui.CodeCompletionSettingsForm;
import com.dci.intellij.dbn.common.options.CompositeProjectConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.util.XmlContents;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettings;
import com.dci.intellij.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CodeCompletionSettings extends CompositeProjectConfiguration<ProjectSettings, CodeCompletionSettingsForm> implements TopLevelConfig {
    private final CodeCompletionFiltersSettings filterSettings = new CodeCompletionFiltersSettings(this);
    private final CodeCompletionSortingSettings sortingSettings = new CodeCompletionSortingSettings(this);
    private final CodeCompletionFormatSettings formatSettings = new CodeCompletionFormatSettings(this);

    public CodeCompletionSettings(ProjectSettings parent) {
        super(parent);
        loadDefaults();
    }

    public static CodeCompletionSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getCodeCompletionSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.CodeCompletionSettings";
    }


    @Override
    public String getDisplayName() {
        return "Code Completion";
    }

    @Override
    public String getHelpTopic() {
        return "codeEditor";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.CODE_COMPLETION;
    }

    private void loadDefaults() {
        Element element = loadDefinition();
        readConfiguration(element);
    }

    @SneakyThrows
    private Element loadDefinition() {
        return XmlContents.fileToElement(getClass(), "default-settings.xml");
    }

    /*********************************************************
     *                     Configuration                      *
     *********************************************************/

    @Override
    @NotNull
    public CodeCompletionSettingsForm createConfigurationEditor() {
        return new CodeCompletionSettingsForm(this);
    }

    @NotNull
    @Override
    public CodeCompletionSettings getOriginalSettings() {
        return CodeCompletionSettings.getInstance(getProject());
    }

    @Override
    public String getConfigElementName() {
        return "code-completion-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[]{
                filterSettings,
                sortingSettings,
                formatSettings};
    }
}
