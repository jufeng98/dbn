package com.dbn.data.record.navigation;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public enum RecordNavigationTarget implements Presentable{
    VIEWER("Record Viewer", null),
    EDITOR("Table Editor", null),
    ASK("Ask", null),
    @Deprecated
    PROMPT("Ask", null);

    private String name;
    private Icon icon;

    RecordNavigationTarget(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return icon;
    }
}
