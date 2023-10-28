package com.dci.intellij.dbn.options;

import com.dci.intellij.dbn.browser.options.DatabaseBrowserSettings;
import com.dci.intellij.dbn.code.common.completion.options.CodeCompletionSettings;
import com.dci.intellij.dbn.code.common.style.options.ProjectCodeStyleSettings;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.options.CompositeProjectConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.options.ConfigurationHandle;
import com.dci.intellij.dbn.common.options.ProjectConfiguration;
import com.dci.intellij.dbn.common.util.Cloneable;
import com.dci.intellij.dbn.connection.config.ConnectionBundleSettings;
import com.dci.intellij.dbn.connection.operation.options.OperationSettings;
import com.dci.intellij.dbn.data.grid.options.DataGridSettings;
import com.dci.intellij.dbn.ddl.options.DDLFileSettings;
import com.dci.intellij.dbn.editor.code.options.CodeEditorSettings;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.execution.common.options.ExecutionEngineSettings;
import com.dci.intellij.dbn.navigation.options.NavigationSettings;
import com.dci.intellij.dbn.options.general.GeneralProjectSettings;
import com.dci.intellij.dbn.options.ui.ProjectSettingsForm;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ProjectSettings
        extends CompositeProjectConfiguration<ProjectConfiguration, ProjectSettingsForm>
        implements SearchableConfigurable.Parent, Cloneable<ProjectSettings> {

    private final GeneralProjectSettings generalSettings           = new GeneralProjectSettings(this);
    private final DatabaseBrowserSettings browserSettings          = new DatabaseBrowserSettings(this);
    private final NavigationSettings navigationSettings            = new NavigationSettings(this);
    private final DataGridSettings dataGridSettings                = new DataGridSettings(this);
    private final DataEditorSettings dataEditorSettings            = new DataEditorSettings(this);
    private final CodeEditorSettings codeEditorSettings            = new CodeEditorSettings(this);
    private final CodeCompletionSettings codeCompletionSettings    = new CodeCompletionSettings(this);
    private final ProjectCodeStyleSettings codeStyleSettings       = new ProjectCodeStyleSettings(this);
    private final ExecutionEngineSettings executionEngineSettings  = new ExecutionEngineSettings(this);
    private final OperationSettings operationSettings              = new OperationSettings(this);
    private final DDLFileSettings ddlFileSettings                  = new DDLFileSettings(this);
    private final ConnectionBundleSettings connectionSettings      = new ConnectionBundleSettings(this);

    public ProjectSettings(Project project) {
        super(project);
    }

    public static ProjectSettings get(Project project) {
        return project.isDefault() ?
                DefaultProjectSettingsManager.getInstance().getProjectSettings() :
                ProjectSettingsManager.getInstance(project).getProjectSettings();
    }

    public static ProjectSettings getDefault() {
        return DefaultProjectSettingsManager.getInstance().getProjectSettings();
    }

    @Override
    public String getHelpTopic() {
        ProjectSettingsForm settingsEditor = getSettingsEditor();
        if (settingsEditor == null) {
            return super.getHelpTopic();
        } else {
            Configuration selectedConfiguration = settingsEditor.getActiveSettings();
            return selectedConfiguration.getHelpTopic();
        }
    }

    @Override
    public void apply() throws ConfigurationException {
        super.apply();
    }

    @NotNull
    @Override
    public JComponent createComponent() {
        return new JPanel();
    }

    public JComponent createCustomComponent() {
        return super.createComponent();
    }


    @Override
    @Nls
    public String getDisplayName() {
        return "Database Navigator";
    }

    @Override
    @Nullable
    public Icon getIcon() {
        return Icons.DATABASE_NAVIGATOR;
    }

    @NotNull
    @Override
    public Configurable[] getConfigurables() {
        return getConfigurations();
    }

    @Nullable
    public Configuration getConfiguration(ConfigId settingsId) {
        for (Configurable configurable : getConfigurables()) {
            TopLevelConfig topLevelConfig = (TopLevelConfig) configurable;
            if (topLevelConfig.getConfigId() == settingsId) {
                return (Configuration) configurable;
            }
        }
        return null;
    }


    /*********************************************************
     *                    Configuration                      *
     *********************************************************/
    @Override
    @NotNull
    public ProjectSettingsForm createConfigurationEditor() {
        return new ProjectSettingsForm(this);
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                connectionSettings,
                browserSettings,
                navigationSettings,
                //codeStyleSettings,
                dataGridSettings,
                dataEditorSettings,
                codeEditorSettings,
                codeCompletionSettings,
                executionEngineSettings,
                operationSettings,
                ddlFileSettings,
                generalSettings};
    }

    /*********************************************************
    *              SearchableConfigurable.Parent             *
    *********************************************************/
    @Override
    public boolean hasOwnContent() {
        return false;
    }

    @Override
    @NotNull
    public String getId() {
        return "DBNavigator.Project.Settings";
    }

    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @Override
    public ProjectSettings clone() {
        try {
            ConfigurationHandle.setTransitory(true);
            Element element = new Element("project-settings");
            writeConfiguration(element);
            ProjectSettings projectSettings = new ProjectSettings(getProject());
            projectSettings.readConfiguration(element);
            return projectSettings;
        } finally {
            ConfigurationHandle.setTransitory(false);
        }
    }
}
