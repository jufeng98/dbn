package com.dci.intellij.dbn.common;

import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.common.dispose.Disposable;
import com.dci.intellij.dbn.common.notification.NotificationSupport;
import com.intellij.openapi.application.ApplicationAdapter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;

public abstract class AbstractProjectComponent extends ApplicationAdapter implements ProjectComponent, ProjectManagerListener, Disposable, NotificationSupport {
    private ProjectRef projectRef;

    protected AbstractProjectComponent(Project project) {
        this.projectRef = ProjectRef.from(project);
        ProjectManager projectManager = ProjectManager.getInstance();
        projectManager.addProjectManagerListener(project, this);
        ApplicationManager.getApplication().addApplicationListener(this);
    }

    @NotNull
    public Project getProject() {
        return projectRef.getnn();
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    public void initComponent() {
    }

    /***********************************************
     *            ProjectManagerListener           *
     ***********************************************/
    @Override
    public void projectOpened(@NotNull Project project) {

    }

    @Override
    public boolean canCloseProject(@NotNull Project project) {
        return true;
    }

    @Override
    public void projectClosed(@NotNull Project project) {

    }

    @Override
    public void projectClosing(@NotNull Project project) {

    }


    /********************************************* *
     *                Disposable                   *
     ***********************************************/
    private boolean disposed = false;

    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void dispose() {
        disposed = true;
    }

    public void disposeComponent() {
        dispose();
    }
}
