package com.dbn.execution;

import com.dbn.common.property.Property;

public enum ExecutionStatus implements Property.IntBase {
    QUEUED,
    PROMPTED,
    EXECUTING,
    CANCELLED,
    CANCEL_REQUESTED;

    public static final ExecutionStatus[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);

    @Override
    public IntMasks masks() {
        return masks;
    }
}
