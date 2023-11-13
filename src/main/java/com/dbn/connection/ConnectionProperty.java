package com.dbn.connection;

import com.dbn.common.property.Property;

public enum ConnectionProperty implements Property.IntBase {
    RS_TYPE_SCROLL_INSENSITIVE,
    RS_TYPE_FORWARD_ONLY;

    public static final ConnectionProperty[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);

    @Override
    public IntMasks masks() {
        return masks;
    }
}
