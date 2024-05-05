package com.dbn.common.ui;

import com.dbn.common.compatibility.Compatibility;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class SpeedSearchBase<C extends JComponent> extends com.intellij.ui.SpeedSearchBase<C> {

    protected SpeedSearchBase(C component) {
        super(component);
    }

    //@Override
    @Compatibility
    protected final Object @NotNull [] getAllElements() {
        return getElements();
    }

    protected abstract Object[] getElements();

    @Override
    protected final int getElementCount() {
        return getElements().length;
    }

    //@Override
    @Compatibility
    protected final Object getElementAt(int viewIndex) {
        return getElements()[viewIndex];
    }
}
