package com.dbn.common;

import java.util.HashSet;
import java.util.Set;

public class ThreadLocalRegister {
    private static final ThreadLocal<Set<Object>> threadLocal = new ThreadLocal<>();

    static {
        threadLocal.set(new HashSet<>());
    }

    private static Set<Object> getRegister() {
        Set<Object> register = threadLocal.get();
        if (register == null) {
            register = new HashSet<>();
            threadLocal.set(register);
        }
        return register;
    }

    public static void register(Object object) {
        getRegister().add(object);
    }

    public static void unregister(Object object) {
        getRegister().remove(object);
    }

    public static boolean isRegistered(Object object) {
        return getRegister().contains(object);
    }
}
