package com.dbn.common.project;

import com.dbn.common.action.UserDataKeys;
import com.dbn.common.ref.WeakRef;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.dispose.Failsafe.nn;

public class ProjectRef extends WeakRef<Project> {
    private ProjectRef(Project project) {
        super(project);
    }

    public static ProjectRef of(Project project) {
        if (project == null) {
            return new ProjectRef(null);
        } else {
            ProjectRef projectRef = project.getUserData(UserDataKeys.PROJECT_REF);
            if (projectRef == null) {
                projectRef = new ProjectRef(project);
                project.putUserData(UserDataKeys.PROJECT_REF, projectRef);
            }
            return projectRef;
        }
    }

    @NotNull
    @Override
    public Project ensure() {
        return nn(super.ensure());
    }
}
