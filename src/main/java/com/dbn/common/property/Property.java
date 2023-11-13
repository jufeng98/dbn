package com.dbn.common.property;

public interface Property {
    @Deprecated
    default PropertyGroup group() {
        return null;
    }

    default boolean implicit() {
        return false;
    }

    interface LongBase extends Property{
        int ordinal();

        LongMasks masks();

        default long maskOn() {
            return masks().on;
        }

        default long maskOff() {
            return masks().off;
        }

        class LongMasks {
            private final long on;
            private final long off;

            public LongMasks(LongBase property) {
                int shift = property.ordinal();
                assert shift < 63;
                this.on = 1L << shift;
                this.off = ~this.on;
            }
        }
    }

    interface IntBase extends Property{
        int ordinal();

        IntMasks masks();

        default int maskOn() {
            return masks().on;
        }

        default int maskOff() {
            return masks().off;
        }

        class IntMasks {
            private final int on;
            private final int off;

            public IntMasks(IntBase property) {
                int shift = property.ordinal();
                assert shift < 32;
                this.on = 1 << shift;
                this.off = ~this.on;
           }
        }
    }
}
