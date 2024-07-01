package com.dbn.nls;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class NlsResources {
    public static final @NonNls String BUNDLE = "nls.NlsResources";
    private static final DynamicBundle INSTANCE = new DynamicBundle(NlsResources.class, BUNDLE);

    public static @Nls String nls(@NonNls @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }
}
