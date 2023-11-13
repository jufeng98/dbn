package com.dbn.connection;

import com.dbn.common.property.Property;

public enum ConnectionHandlerStatus implements Property.IntBase {
    CONNECTED,
    CLEANING,
    LOADING,
    ACTIVE,
    VALID,
    BUSY;

    public static final ConnectionHandlerStatus[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);

    @Override
    public IntMasks masks() {
        return masks;
    }
}
