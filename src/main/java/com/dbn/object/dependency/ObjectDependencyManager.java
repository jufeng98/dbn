package com.dbn.object.dependency;

import com.dbn.DatabaseNavigator;
import com.dbn.common.component.Components;
import com.dbn.common.component.PersistentState;
import com.dbn.common.component.ProjectComponentBase;
import com.dbn.common.options.setting.Settings;
import com.dbn.common.util.Dialogs;
import com.dbn.connection.ConnectionAction;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.dependency.ui.ObjectDependencyTreeDialog;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@State(
    name = ObjectDependencyManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class ObjectDependencyManager extends ProjectComponentBase implements PersistentState {
    public static final String COMPONENT_NAME = "DBNavigator.Project.ObjectDependencyManager";

    private ObjectDependencyType lastUserDependencyType = ObjectDependencyType.INCOMING;

    private ObjectDependencyManager(final Project project) {
        super(project, COMPONENT_NAME);
    }

    public static ObjectDependencyManager getInstance(@NotNull Project project) {
        return Components.projectService(project, ObjectDependencyManager.class);
    }

    public ObjectDependencyType getLastUserDependencyType() {
        return lastUserDependencyType;
    }

    public void setLastUserDependencyType(ObjectDependencyType lastUserDependencyType) {
        this.lastUserDependencyType = lastUserDependencyType;
    }

    public void openDependencyTree(DBSchemaObject schemaObject) {
        ConnectionAction.invoke("opening object dependency tree", false, schemaObject,
                action -> Dialogs.show(() -> new ObjectDependencyTreeDialog(getProject(), schemaObject)));
    }

    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        Settings.setEnum(element, "last-used-dependency-type", lastUserDependencyType);
        return element;
    }

    @Override
    public void loadComponentState(@NotNull final Element element) {
        lastUserDependencyType = Settings.getEnum(element, "last-used-dependency-type", lastUserDependencyType);
    }

}
