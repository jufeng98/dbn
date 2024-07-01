package com.dbn.nls;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public interface NlsSupport {
    default @Nls String nls(@Nls @PropertyKey(resourceBundle = NlsResources.BUNDLE) String key, Object @NotNull ... params) {
        return NlsResources.nls(key, params);
    }

}
