package com.dbn.common.ui.form;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public abstract class DBNToolbarForm extends DBNFormBase{
    public DBNToolbarForm(@Nullable Disposable parent, Project project) {
        super(parent, project);
    }
}
