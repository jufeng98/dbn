package com.dci.intellij.dbn.common.util;

import com.dci.intellij.dbn.common.routine.ParametricCallable;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import static com.dci.intellij.dbn.common.util.Unsafe.cast;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@UtilityClass
public class Classes {

    public static <P, R, E extends Throwable> R withClassLoader(P param, ParametricCallable<P, R, E> callable) throws E{
        Thread thread = Thread.currentThread();
        ClassLoader currentClassLoader = thread.getContextClassLoader();
        try {
            ClassLoader paramClassLoader = param.getClass().getClassLoader();
            thread.setContextClassLoader(paramClassLoader);
            return callable.call(param);
        } finally {
            thread.setContextClassLoader(currentClassLoader);
        }
    }

    @Nullable
    public static <T> Class<T> classForName(String name) {
        try {
            return cast(Class.forName(name));
        } catch (Throwable e) {
            conditionallyLog(e);
            return null;
        }
    }
}
