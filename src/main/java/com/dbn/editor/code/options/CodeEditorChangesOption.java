package com.dbn.editor.code.options;

import com.dbn.common.option.InteractiveOption;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum CodeEditorChangesOption implements InteractiveOption {
    ASK("Ask"),
    SAVE("Save"),
    DISCARD("Discard"),
    SHOW("Show Changes"),
    CANCEL("Cancel");

    private String name;

    CodeEditorChangesOption(String name) {
        this.name = name;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }


    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }


    public static CodeEditorChangesOption get(String name) {
        for (CodeEditorChangesOption option : CodeEditorChangesOption.values()) {
            if (Objects.equals(option.name, name) || Objects.equals(option.name(), name)) {
                return option;
            }
        }
        return null;
    }}
