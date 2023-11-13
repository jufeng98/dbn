package com.dbn.common.util;

import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.dialog.DBNDialog;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@UtilityClass
public class Dialogs {

    public static <T extends DBNDialog> void show(@NotNull Supplier<T> builder) {
        show(builder, null);
    }

    public static <T extends DBNDialog> void show(@NotNull Supplier<T> builder, @Nullable DialogCallback<T> callback) {
        Dispatch.run(() -> {
            T dialog = builder.get();
            dialog.show();

            if (callback != null) {
                int exitCode = dialog.getExitCode();
                callback.call(dialog, exitCode);
            }
        });
    }

    @FunctionalInterface
    public interface DialogCallback<T extends DBNDialog> {
        void call(T dialog, int exitCode);
    }

}
