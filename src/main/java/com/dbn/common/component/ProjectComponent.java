package com.dbn.common.component;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface ProjectComponent extends Service {
    @NotNull
    Project getProject();
}
