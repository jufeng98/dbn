package com.dci.intellij.dbn.common.expression;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.common.component.Components;
import com.dci.intellij.dbn.common.component.PersistentState;
import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@State(
        name = ExpressionEvaluationManager.COMPONENT_NAME,
        storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class ExpressionEvaluationManager extends ProjectComponentBase implements PersistentState {

    public static final String COMPONENT_NAME = "DBNavigator.Project.ExpressionEvaluationManager";

    private ExpressionEvaluationManager(Project project) {
        super(project, COMPONENT_NAME);
    }

    public static ExpressionEvaluationManager getInstance(@NotNull Project project) {
        return Components.projectService(project, ExpressionEvaluationManager.class);
    }

    @Override
    public Element getComponentState() {
        return null;
    }

    @Override
    public void loadComponentState(@NotNull final Element element) {
    }

    @Override
    public void disposeInner() {
        super.disposeInner();
    }
}