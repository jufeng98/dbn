package com.dbn.editor.data;

import com.dbn.common.property.Property;

public enum DatasetLoadInstruction implements Property.IntBase {
    USE_CURRENT_FILTER,
    PRESERVE_CHANGES,
    DELIBERATE_ACTION,
    REBUILD;

    public static final DatasetLoadInstruction[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);

    @Override
    public IntMasks masks() {
        return masks;
    }
}
