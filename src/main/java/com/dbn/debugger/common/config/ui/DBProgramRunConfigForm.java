package com.dbn.debugger.common.config.ui;

import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.debugger.common.config.DBRunConfig;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;

public abstract class DBProgramRunConfigForm<T extends DBRunConfig> extends DBNFormBase {

    public DBProgramRunConfigForm(Project project) {
        super(null, project);
    }

    public abstract void readConfiguration(T configuration);

    public abstract void writeConfiguration(T configuration) throws ConfigurationException;
}
