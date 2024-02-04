package com.dbn.common;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dbn.common.util.Commons.nvl;
import static com.dbn.common.util.Unsafe.cast;

@UtilityClass
public class Reflection {
    private static final Map<Class, Class> enclosingClasses = new ConcurrentHashMap<>();
    private static final Map<Class, String> classNames = new ConcurrentHashMap<>();
    private static final Map<Class, String> simpleClassNames = new ConcurrentHashMap<>();

    public static Class<?> getEnclosingClass(Class clazz) {
        return enclosingClasses.computeIfAbsent(clazz, c -> nvl(c.getEnclosingClass(), c));
    }

    public static String getClassName(Class clazz) {
        return classNames.computeIfAbsent(clazz, c -> c.getName());
    }

    public static String getSimpleClassName(Class clazz) {
        return simpleClassNames.computeIfAbsent(clazz, c -> c.getSimpleName());
    }

    @SneakyThrows
    public static <T> T invokeMethod(Object object, String methodName, Object... args) {
        Class[] argTypes = Arrays.stream(args).map(a -> a.getClass()).toArray(Class[]::new);
        Method method = object.getClass().getMethod(methodName, argTypes);
        return cast(method.invoke(object, args));
    }

}
