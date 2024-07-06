package com.dbn.debugger.common.settings;

import com.dbn.common.compatibility.Compatibility;
import com.intellij.openapi.options.Configurable;
import com.intellij.xdebugger.settings.DebuggerSettingsCategory;
import com.intellij.xdebugger.settings.XDebuggerSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class DBProgramDebuggerSettings extends XDebuggerSettings<DBProgramDebuggerState> {
    DBProgramDebuggerState state = new DBProgramDebuggerState();

    protected DBProgramDebuggerSettings() {
        super("db-program");
    }

    @SuppressWarnings("removal")
    @NotNull
    @Override
    @Compatibility
    public Configurable createConfigurable() {
        return new DBProgramDebuggerConfigurable();
    }

    @Override
    public @NotNull Collection<? extends Configurable> createConfigurables(@NotNull DebuggerSettingsCategory category) {
        if (category == DebuggerSettingsCategory.ROOT) {
            return Collections.singleton(new DBProgramDebuggerConfigurable());
        }
        return super.createConfigurables(category);
    }

    @Override
    public DBProgramDebuggerState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull DBProgramDebuggerState state) {
        this.state = state;
    }

}
