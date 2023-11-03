package com.dci.intellij.dbn.status;

import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import org.jetbrains.annotations.NotNull;

public class ConnectionLoadStatusBarWidget extends ProjectComponentBase implements StatusBarWidget{

    public static final String COMPONENT_NAME = "DBNavigator.Project.ConnectionLoadStatus";

    public ConnectionLoadStatusBarWidget(Project project) {
        super(project, COMPONENT_NAME);
    }

    @NotNull
    @Override
    public String ID() {
        return "DBNavigator.ConnectionLoadStatus";
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {

    }

    @Override
    public void dispose() {
        if (isDisposed()) return;
        setDisposed(true);

        StatusBarWidget.super.dispose();
        disposeInner();
    }
}
