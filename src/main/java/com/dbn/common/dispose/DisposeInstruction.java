package com.dbn.common.dispose;

import com.dbn.common.property.Property;
import com.dbn.common.property.PropertyHolderBase;

public enum DisposeInstruction implements Property.IntBase {
    REGISTERED,
    BACKGROUND,
    CLEAR,
    NULLIFY;

    public static final DisposeInstruction[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);

    @Override
    public IntMasks masks() {
        return masks;
    }

    public static class Bundle extends PropertyHolderBase.IntStore<DisposeInstruction> {
        public Bundle(DisposeInstruction... instructions) {
            for (DisposeInstruction instruction : instructions) {
                set(instruction, true);
            }
        }

        @Override
        protected DisposeInstruction[] properties() {
            return VALUES;
        }
    }

    public static Bundle from(DisposeInstruction ... instructions) {
        return new Bundle(instructions);
    }
}
