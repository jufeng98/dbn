package com.dbn.common.component;

import com.dbn.common.notification.NotificationSupport;
import com.dbn.common.dispose.StatefulDisposableBase;
import com.dbn.common.project.ProjectRef;
import com.dbn.common.project.Projects;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class ProjectComponentBase extends StatefulDisposableBase implements
        ProjectComponent,
        NotificationSupport {

    private final ProjectRef project;
    private final String componentName;

    protected ProjectComponentBase(@NotNull Project project, String componentName) {
        this.project = ProjectRef.of(project);
        this.componentName = componentName;
        ProjectManagerListener.register(this);
    }

    @NotNull
    public final String getComponentName() {
        return componentName;
    }

    @Override
    @NotNull
    public Project getProject() {
        return project.ensure();
    }

    protected void closeProject(boolean exitApp) {
        if (exitApp) {
            ApplicationManager.getApplication().exit();
        } else {
            Projects.closeProject(getProject());
        }
    }

    @Override
    public void checkDisposed() {
        super.checkDisposed();
        getProject();
    }
}
