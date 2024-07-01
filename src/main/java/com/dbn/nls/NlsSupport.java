package com.dbn.nls;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public interface NlsSupport {
    default @Nls String nls(@Nls @PropertyKey(resourceBundle = NlsResources.BUNDLE) String key, Object @NotNull ... params) {
        return NlsResources.nls(key, params);
    }

    default @Nls String nls(@Nls @PropertyKey(resourceBundle = NlsResources.BUNDLE) String key, Presentable @NotNull ... params) {
        return NlsResources.nls(key, toObjectArray(params));
    }

    static Object[] toObjectArray(Presentable... presentables) {
        Object[] objects = new Object[presentables.length];
        for (int i = 0; i < presentables.length; i++) {
            Presentable presentable = presentables[i];
            objects[i] = presentable.getName();
        }
        return objects;
    }
}
