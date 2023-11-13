package com.dbn.options;

import com.dbn.DatabaseNavigator;
import com.dbn.common.component.ApplicationComponentBase;
import com.dbn.common.component.PersistentState;
import com.dbn.common.project.Projects;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Getter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.component.Components.applicationService;

@Getter
@State(
    name = "DBNavigator.DefaultProject.Settings",
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class DefaultProjectSettingsManager extends ApplicationComponentBase implements PersistentState {

    private final ProjectSettings projectSettings;

    private DefaultProjectSettingsManager() {
        super("DBNavigator.Application.TemplateProjectSettings");
        projectSettings = new ProjectSettings(Projects.getDefaultProject());
    }

    public static DefaultProjectSettingsManager getInstance() {
        return applicationService(DefaultProjectSettingsManager.class);
    }

    /****************************************
     *       PersistentStateComponent       *
     *****************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        projectSettings.writeConfiguration(element);
        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        projectSettings.readConfiguration(element);
    }
}
