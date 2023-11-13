package com.dbn.execution;

import com.dbn.common.option.InteractiveOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Deprecated
public enum TargetConnectionOption implements InteractiveOption{
    ASK("Ask"),
    MAIN("Main connection"),
    POOL("Pool connection (async)"),
    CANCEL("Cancel");

    private String name;

    TargetConnectionOption(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public String getDescription() {
        return null;
    }


    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }
}
