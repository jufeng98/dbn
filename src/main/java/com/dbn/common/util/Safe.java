package com.dbn.common.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.dbn.common.dispose.Checks.isNotValid;
import static com.dbn.common.util.Unsafe.cast;

@UtilityClass
public final class Safe {

    public static <R, T> R call(@Nullable T target, Function<T, R> supplier, R defaultValue) {
        if (isNotValid(target)) return defaultValue;
        return supplier.apply(target);
    }

    @Nullable
    public static <R, T> R call(@Nullable T target, @NotNull Function<T, R> supplier){
        if (isNotValid(target)) return null;
        return supplier.apply(target);
    }

    public static <T> void run(@Nullable T target, @NotNull Consumer<T> runnable){
        if (isNotValid(target)) return;
        runnable.accept(target);
    }

    public static <T extends Comparable<T>> int compare(@Nullable T value1, @Nullable T value2) {
        if (value1 == null && value2 == null) {
            return 0;
        }
        if (value1 == null) {
            return -1;
        }

        if (value2 == null) {
            return 1;
        }

        return value1.compareTo(value2);
    }

    public static <T, C extends T, R> R call(@Nullable T target, Class<C> targetClass, Function<C, R> supplier, R defaultValue) {
        if (isNotValid(target)) return defaultValue;
        if (!targetClass.isAssignableFrom(target.getClass())) return defaultValue;
        return supplier.apply(cast(target));
    }

    public static <T, C extends T> void run(@Nullable T target, Class<C> targetClass, Consumer<C> runnable) {
        if (isNotValid(target)) return;
        if (!targetClass.isAssignableFrom(target.getClass())) return;
        runnable.accept(cast(target));
    }

}
