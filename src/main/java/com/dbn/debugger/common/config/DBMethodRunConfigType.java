package com.dbn.debugger.common.config;

import com.dbn.common.icon.Icons;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.jdbc.config.DBMethodJdbcRunConfigFactory;
import com.dbn.debugger.jdwp.config.DBJdwpMethodRunConfigFactory;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@Getter
public class DBMethodRunConfigType extends DBRunConfigType<DBMethodRunConfigFactory> {
    public static final String DEFAULT_RUNNER_NAME = "DB Method Runner";

    private final DBMethodRunConfigFactory[] configurationFactories = new DBMethodRunConfigFactory[]{
            new DBMethodJdbcRunConfigFactory(this),
            new DBJdwpMethodRunConfigFactory(this)};


    @NotNull
    @Override
    public String getDisplayName() {
        return "DB Method";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "DB Navigator - Method Runner";
    }

    @Override
    public Icon getIcon() {
        return Icons.EXEC_METHOD_CONFIG;
    }

    @Override
    @NotNull
    public String getId() {
        return "DBNMethodRunConfiguration";
    }

    @Override
    public String getDefaultRunnerName() {
        return DEFAULT_RUNNER_NAME;
    }

    @Override
    public DBMethodRunConfigFactory getConfigurationFactory(DBDebuggerType debuggerType) {
        for (DBMethodRunConfigFactory configurationFactory : configurationFactories) {
            if (configurationFactory.getDebuggerType() == debuggerType) {
                return configurationFactory;
            }
        }
        return null;
    }
}
