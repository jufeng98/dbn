package com.dbn.common.options;

import com.dbn.common.dispose.Disposer;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.project.ProjectRef;
import com.dbn.project.ProjectStateManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public abstract class BasicProjectConfiguration<P extends ProjectConfiguration<?, ?>, E extends ConfigurationEditorForm<?>>
        extends BasicConfiguration<P, E>
        implements ProjectConfiguration<P, E> {

    private ProjectRef project;

    public BasicProjectConfiguration(@NotNull P parent) {
        super(parent);
        Disposer.register(parent, this);
    }

    public BasicProjectConfiguration(@NotNull Project project) {
        super(null);
        this.project = ProjectRef.of(project);
        ProjectStateManager.registerDisposable(project, this);
    }

    @SuppressWarnings("DataFlowIssue")
    @NotNull
    @Override
    public Project getProject() {
        if (project != null) {
            return project.ensure();
        }

        return getParent().ensureProject();
    }
}
