package com.dbn.common.component;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.dispose.Failsafe.nd;

public class Components {
    private Components() {
    }

    @NotNull
    public static <T extends ProjectComponent> T projectService(@NotNull Project project, @NotNull Class<T> interfaceClass) {
        return nd(project).getService(interfaceClass);
    }

    @NotNull
    public static <T extends ApplicationComponent> T applicationService(@NotNull Class<T> interfaceClass) {
        Application application = ApplicationManager.getApplication();
        return application.getService(interfaceClass);
    }

}
