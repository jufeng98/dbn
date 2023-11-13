package com.dbn.language.common.psi;

import com.dbn.common.property.Property;

public enum PsiResolveStatus implements Property.IntBase {
    NEW,
    RESOLVING,
    RESOLVING_OBJECT_TYPE,
    CONNECTION_VALID,
    CONNECTION_ACTIVE;

    public static final PsiResolveStatus[] VALUES = values();
    private final IntMasks masks = new IntMasks(this);

    @Override
    public IntMasks masks() {
        return masks;
    }
}
