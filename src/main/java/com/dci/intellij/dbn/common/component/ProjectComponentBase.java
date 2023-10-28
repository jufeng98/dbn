package com.dci.intellij.dbn.common.component;

import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.notification.NotificationSupport;
import com.dci.intellij.dbn.common.project.ProjectRef;
import com.dci.intellij.dbn.common.project.Projects;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class ProjectComponentBase extends StatefulDisposableBase implements
        ProjectComponent,
        StatefulDisposable,
        NotificationSupport,
        Service {

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
    public void disposeInner() {
        nullify();
    }

    @Override
    public void checkDisposed() {
        super.checkDisposed();
        getProject();
    }
}
