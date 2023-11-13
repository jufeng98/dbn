package com.dbn.common.ui.component;

import com.dbn.common.action.Lookups;
import com.dbn.common.ref.WeakRef;
import com.dbn.common.dispose.Disposer;
import com.dbn.common.dispose.StatefulDisposableBase;
import com.dbn.common.project.ProjectRef;
import com.dbn.common.project.ProjectSupplier;
import com.dbn.common.ui.dialog.DBNDialog;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public abstract class DBNComponentBase extends StatefulDisposableBase implements DBNComponent {
    private final ProjectRef project;
    private WeakRef<Disposable> parent;

    public DBNComponentBase(@Nullable Disposable parent) {
        this.parent = WeakRef.of(parent);
        this.project = null;

        registerDisposable(parent);
    }

    @Deprecated // load project from data context
    public DBNComponentBase(Disposable parent, @Nullable Project project) {
        this.parent = WeakRef.of(parent);
        this.project = ProjectRef.of(project);
        registerDisposable(parent);
    }

    public final void setParent(Disposable parent) {
        this.parent = WeakRef.of(parent);
        registerDisposable(parent);
    }

    private void registerDisposable(Disposable parent) {
        if (parent instanceof DBNDialog) {
            DBNDialog dialog = (DBNDialog) parent;
            Disposer.register(dialog.getDisposable(), this);
        } else {
            Disposer.register(parent, this);
        }
    }

    @Nullable
    @Override
    public final <T extends Disposable> T getParentComponent() {
        return (T) WeakRef.get(parent);
    }

    @Override
    @Nullable
    public final Project getProject() {
        if (project != null) {
            return project.ensure();
        }

        if (this.parent != null) {
            Disposable parent = this.parent.ensure();

            if (parent instanceof ProjectSupplier) {
                ProjectSupplier component = (ProjectSupplier) parent;
                Project project = component.getProject();
                if (project != null) {
                    return project;
                }
            }
        }

        return Lookups.getProject(getComponent());
    }
}
