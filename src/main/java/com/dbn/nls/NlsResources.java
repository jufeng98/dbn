package com.dbn.nls;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class NlsResources extends DynamicBundle {
    public static final @NonNls String BUNDLE = "nls.NlsResources";
    private static final NlsResources INSTANCE = new NlsResources(BUNDLE);

    public NlsResources(@NotNull String pathToBundle) {
        super(pathToBundle);
    }

    public static @Nls String nls(@NonNls @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }
}
