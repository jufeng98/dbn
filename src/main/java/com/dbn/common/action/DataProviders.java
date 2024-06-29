package com.dbn.common.action;

import com.dbn.common.compatibility.Workaround;
import com.dbn.common.dispose.Checks;
import com.dbn.common.ref.WeakRef;
import com.intellij.ide.DataManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Workaround
public class DataProviders {

    public static void register(@NotNull JComponent component, @NotNull DataProvider provider) {
        if (component instanceof com.intellij.openapi.actionSystem.DataProvider) return;

        DataProviderDelegate delegate = new DataProviderDelegate(provider);
        DataManager.registerDataProvider(component, delegate);
    }

    private static class DataProviderDelegate implements com.intellij.openapi.actionSystem.DataProvider {
        private final WeakRef<DataProvider> delegate;

        private DataProviderDelegate(DataProvider delegate) {
            this.delegate = WeakRef.of(delegate);
        }

        @Nullable
        @Override
        public Object getData(@NotNull @NonNls String name) {
            DataProvider dataProvider = delegate.get();
            if (dataProvider == null) return null;
            if (Checks.isNotValid(dataProvider)) {
                delegate.clear();
                return null;
            }
            return dataProvider.getData(name);
        }
    }
}
