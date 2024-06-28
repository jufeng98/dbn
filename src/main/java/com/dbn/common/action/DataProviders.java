package com.dbn.common.action;

import com.dbn.common.Reflection;
import com.dbn.common.compatibility.Workaround;
import com.dbn.common.dispose.Checks;
import com.dbn.common.dispose.Disposer;
import com.dbn.common.ref.WeakRef;
import com.dbn.common.util.Unsafe;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dbn.common.Reflection.*;

public class DataProviders {

    public static void register(@NotNull JComponent component, @NotNull DataProvider provider) {
        if (component instanceof DataProvider) return;

        DataProviderDelegate delegate = new DataProviderDelegate(provider);
        DataManager.registerDataProvider(component, delegate);
    }

    private static class DataProviderDelegate implements DataProvider {
        private final WeakRef<DataProvider> delegate;
        private final Map<Class, Method> delegateMethods = new ConcurrentHashMap<>();

        private DataProviderDelegate(DataProvider delegate) {
            this.delegate = WeakRef.of(delegate);
        }

        @Nullable
        @Override
        @Workaround
        public Object getData(@NotNull @NonNls String s) {
            DataProvider dataProvider = delegate.get();
            if (dataProvider == null) return null;
            if (Checks.isNotValid(dataProvider)) {
                delegate.clear();
                return null;
            }

            // REFLECTION: circumvent OverrideOnly assertions (this is just a weak-ref delegate - no other option)
            Method delegateMethod = delegateMethods.computeIfAbsent(dataProvider.getClass(), c -> findMethod(c, "getData", String.class));
            if(delegateMethod == null) return null;

            return Unsafe.silent(null, () -> invokeMethod(dataProvider, delegateMethod, s));
        }
    }
}
