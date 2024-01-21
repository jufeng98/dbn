package com.dbn.debugger.common.config;

import com.dbn.common.icon.Icons;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.jdbc.config.DBStatementJdbcRunConfigFactory;
import com.dbn.debugger.jdwp.config.DBJdwpStatementRunConfigFactory;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@Getter
public class DBStatementRunConfigType extends DBRunConfigType<DBStatementRunConfigFactory> {
    public static final String DEFAULT_RUNNER_NAME = "DB Statement Runner";
    private final DBStatementRunConfigFactory[] configurationFactories = new DBStatementRunConfigFactory[]{
            new DBStatementJdbcRunConfigFactory(this),
            new DBJdwpStatementRunConfigFactory(this)};


    @NotNull
    @Override
    public String getDisplayName() {
        return "DB Statement";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "DB Navigator - Statement Runner";
    }

    @Override
    public Icon getIcon() {
        return Icons.EXEC_STATEMENT_CONFIG;
    }

    @Override
    @NotNull
    public String getId() {
        return "DBNStatementRunConfiguration";
    }

    @Override
    public String getDefaultRunnerName() {
        return DEFAULT_RUNNER_NAME;
    }

    @Override
    public DBStatementRunConfigFactory getConfigurationFactory(DBDebuggerType debuggerType) {
        for (DBStatementRunConfigFactory configurationFactory : configurationFactories) {
            if (configurationFactory.getDebuggerType() == debuggerType) {
                return configurationFactory;
            }
        }
        return null;
    }
}
