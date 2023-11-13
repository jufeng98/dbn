package com.dbn.common.ui;

import com.dbn.common.property.Property;

public enum ValueSelectorOption implements Property.IntBase {
    HIDE_ICON,
    HIDE_DESCRIPTION;

    public static final ValueSelectorOption[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);

    @Override
    public IntMasks masks() {
        return masks;
    }
}
