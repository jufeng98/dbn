package com.dbn.common.ui.dialog;

import com.dbn.common.dispose.Checks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class DBNDialogRegistry {
    private static final Map<Object, DBNDialog> cache = new ConcurrentHashMap<>();

    public static <T extends DBNDialog> T ensure(Object key, Supplier<T> provider) {
        DBNDialog dialog = cache.compute(key, (k, d) -> Checks.isValid(d) ? d : provider.get());
        dialog.addDialogListener(action -> {
            if (action == DBNDialogListener.Action.CLOSE) cache.remove(key);
        });
        return (T) dialog;
    }

}
