package com.dbn.common.action;

import com.dbn.common.ref.WeakRef;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DataProviders {

    public static void register(@NotNull JComponent component, @NotNull DataProvider provider) {
        if (component instanceof DataProvider) return;

        DataProviderDelegate delegate = new DataProviderDelegate(provider);
        DataManager.registerDataProvider(component, delegate);
    }

    private static class DataProviderDelegate implements DataProvider {
        private final WeakRef<DataProvider> delegate;

        private DataProviderDelegate(DataProvider delegate) {
            this.delegate = WeakRef.of(delegate);
        }

        @Override
        public @Nullable
        Object getData(@NotNull @NonNls String s) {
            DataProvider dataProvider = delegate.get();
            return dataProvider == null ? null : dataProvider.getData(s);
        }
    }
}
