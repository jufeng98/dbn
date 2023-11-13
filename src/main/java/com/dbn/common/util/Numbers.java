package com.dbn.common.util;

public interface Numbers {
    static short timeSignature() {
        return (short) (System.currentTimeMillis() % 10000);
    }
}
