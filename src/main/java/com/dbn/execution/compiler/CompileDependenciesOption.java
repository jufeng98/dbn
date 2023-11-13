package com.dbn.execution.compiler;

import com.dbn.common.option.InteractiveOption;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public enum CompileDependenciesOption implements InteractiveOption {
    YES("Yes", true),
    NO("No", true),
    ASK("Ask", false);

    private final String name;
    private final boolean persistable;

    CompileDependenciesOption(String name, boolean persistable) {
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
        return this == ASK;
    }


    public static CompileDependenciesOption get(String name) {
        return Arrays
                .stream(CompileDependenciesOption.values())
                .filter(option -> Objects.equals(option.name, name) || Objects.equals(option.name(), name))
                .findFirst()
                .orElse(null);
    }}
