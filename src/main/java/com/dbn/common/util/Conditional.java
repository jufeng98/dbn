package com.dbn.common.util;


import com.dbn.common.routine.ParametricRunnable;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.dispose.Checks.isNotValid;

@UtilityClass
public class Conditional {

    public static void when(boolean condition, @Nullable Runnable runnable) {
        if (runnable == null) return;
        if (!condition) return;

        runnable.run();
    }

    public static <T, E extends Throwable> void whenValid(@Nullable T object, ParametricRunnable<T, E> runnable) throws E{
        if (runnable == null) return;
        if (isNotValid(object)) return;

        runnable.run(object);
    }

    public static <T, E extends Throwable> void whenNotNull(@Nullable T object, ParametricRunnable<T, E> runnable) throws E{
        if (runnable == null) return;
        if (object == null) return;

        runnable.run(object);
    }

    public static <E extends Throwable> void whenNotEmpty(@Nullable String string, ParametricRunnable<String, E> runnable) throws E{
        if (runnable == null) return;
        if (Strings.isEmptyOrSpaces(string)) return;

        runnable.run(string);
    }
}
