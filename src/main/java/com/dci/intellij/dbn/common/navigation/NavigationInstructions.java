package com.dci.intellij.dbn.common.navigation;

import com.dci.intellij.dbn.common.property.PropertyHolderBase;

import static com.dci.intellij.dbn.common.navigation.NavigationInstruction.*;

public class NavigationInstructions extends PropertyHolderBase.IntStore<NavigationInstruction> {

    public static final NavigationInstructions NONE = new NavigationInstructions();

    private NavigationInstructions(NavigationInstruction ... instructions) {
        super(instructions);
    }

    public static NavigationInstructions create(NavigationInstruction ... instructions) {
        return new NavigationInstructions(instructions);
    }

    public boolean isOpen() {
        return is(OPEN);
    }

    public boolean isFocus() {
        return is(FOCUS);
    }

    public boolean isScroll() {
        return is(SCROLL);
    }

    public boolean isSelect() {
        return is(SELECT);
    }

    public boolean isReset() {
        return is(RESET);
    }

    public NavigationInstructions with(NavigationInstruction instruction) {
        return with(instruction, true);
    }

    public NavigationInstructions without(NavigationInstruction instruction) {
        return with(instruction, false);
    }

    public NavigationInstructions with(NavigationInstruction instruction, boolean value) {
        set(instruction, value);
        return this;
    }

    @Override
    protected NavigationInstruction[] properties() {
        return VALUES;
    }
}
