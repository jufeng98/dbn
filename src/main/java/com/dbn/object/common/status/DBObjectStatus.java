package com.dbn.object.common.status;

import com.dbn.common.property.Property;

public enum DBObjectStatus implements Property.IntBase {
    PRESENT(false, true),
    ENABLED(true, true),
    EDITABLE(false, false),
    VALID(true, true),
    DEBUG(true, true),
    COMPILING(false, false);

    public static final DBObjectStatus[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);
    private final boolean propagable;
    private final boolean defaultValue;

    DBObjectStatus(boolean propagable, boolean defaultValue) {
        this.propagable = propagable;
        this.defaultValue = defaultValue;
    }

    @Override
    public IntMasks masks() {
        return masks;
    }

    public boolean isPropagable() {
        return propagable;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}
