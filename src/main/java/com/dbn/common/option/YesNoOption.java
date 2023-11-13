package com.dbn.common.option;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum YesNoOption implements InteractiveOption {
    YES("Yes", true),
    NO("No", true);

    private String name;
    private boolean persistable;

    YesNoOption(String name, boolean persistable) {
        this.name = name;
        this.persistable = persistable;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    public boolean isCancel() {
        return false;
    }

    @Override
    public boolean isAsk() {
        return false;
    }


    public static YesNoOption get(String name) {
        for (YesNoOption option : YesNoOption.values()) {
            if (Objects.equals(option.name, name) || Objects.equals(option.name(), name)) {
                return option;
            }
        }
        return null;
    }}
