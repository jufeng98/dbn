package com.dbn.mybatis.settings;

import com.dbn.common.options.CompositeProjectConfiguration;
import com.dbn.common.options.Configuration;
import com.dbn.mybatis.ui.MyBatisSettingsForm;
import com.dbn.options.ConfigId;
import com.dbn.options.ProjectSettings;
import com.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class MyBatisSettings extends CompositeProjectConfiguration<ProjectSettings, MyBatisSettingsForm>
        implements TopLevelConfig {
    private final @Getter(lazy = true) GeneratorSettings generatorSettings = new GeneratorSettings(this);

    public MyBatisSettings(ProjectSettings parent) {
        super(parent);
    }

    public static MyBatisSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getMyBatisSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.MyBatisSettings";
    }

    @Override
    public String getDisplayName() {
        return nls("cfg.ddlFiles.title.myBatisGeneratorFiles");
    }

    @Override
    public String getHelpTopic() {
        return "myBatisSettings";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.MYBATIS;
    }

    @NotNull
    @Override
    public MyBatisSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /********************************************************
     *                     Configuration                     *
     *********************************************************/
    @NotNull
    @Override
    public MyBatisSettingsForm createConfigurationEditor() {
        return new MyBatisSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "mybatis-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[]{
                getGeneratorSettings()
        };
    }


}
