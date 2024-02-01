package com.dbn.common.util;

import com.dbn.common.compatibility.Compatibility;

public class Arrays {
    /**
     * Copy of {@link java.util.Arrays#compare(byte[], byte[])}
     *
     */
    @Compatibility
    public static int compare(byte[] a, byte[] b){
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int min = Math.min(a.length, b.length);
        for (int i = 0; i < min; i++) {
            int comparison = Byte.compare(a[i], b[i]);
            if (comparison != 0) {
                return comparison;
            }
        }
        return a.length - b.length;
    }
}
