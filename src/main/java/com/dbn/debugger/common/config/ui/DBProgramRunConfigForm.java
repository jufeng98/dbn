package com.dbn.debugger.common.config.ui;

import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.common.config.DBRunConfig;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import lombok.Getter;

@Getter
public abstract class DBProgramRunConfigForm<T extends DBRunConfig<?>> extends DBNFormBase {
    private final DBDebuggerType debuggerType;


    public DBProgramRunConfigForm(Project project, DBDebuggerType debuggerType) {
        super(null, project);
        this.debuggerType = debuggerType;
    }

    public abstract void readConfiguration(T configuration);

    public abstract void writeConfiguration(T configuration) throws ConfigurationException;
}
