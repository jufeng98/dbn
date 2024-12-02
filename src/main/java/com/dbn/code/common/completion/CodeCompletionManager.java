package com.dbn.code.common.completion;

import com.dbn.DatabaseNavigator;
import com.dbn.common.component.PersistentState;
import com.dbn.common.component.ProjectComponentBase;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.component.Components.projectService;

@State(
    name = CodeCompletionManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class CodeCompletionManager extends ProjectComponentBase implements PersistentState {
    public static final String COMPONENT_NAME = "DBNavigator.Project.CodeCompletionManager";

    private CodeCompletionManager(Project project) {
        super(project, COMPONENT_NAME);
    }

    public static CodeCompletionManager getInstance(@NotNull Project project) {
        return projectService(project, CodeCompletionManager.class);
    }

    /*********************************************
     *            PersistentStateComponent       *
     *********************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        return null;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
    }
}
