package com.dbn.nls;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

public class NlsResources {
    public static final @NonNls String BUNDLE = "nls.NlsResources";
    private static final ResourceBundle INSTANCE = ResourceBundle.getBundle(BUNDLE);

    public static @Nls String nls(@NonNls @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return AbstractBundle.message(INSTANCE, key, params);
    }
}
