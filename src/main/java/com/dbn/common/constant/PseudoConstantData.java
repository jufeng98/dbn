package com.dbn.common.constant;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PseudoConstantData<T extends PseudoConstant<T>> {
    static final ThreadLocal<PseudoConstantData> LOCAL = new ThreadLocal<>();

    private final Class<T> type;
    private final Map<String, T> mappings = new ConcurrentHashMap<>();
    private final List<T> values = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public PseudoConstantData(Class<T> type) {
        this.type = type;
        init();
    }

    private void init() {
        try {
            lock.lock();
            LOCAL.set(this);
            createConstant(null);
        } finally {
            LOCAL.remove();
            lock.unlock();
        }
    }

    T get(String id) {
        T constant = mappings.get(id);
        if (constant != null) return constant;

        try {
            lock.lock();
            constant = mappings.get(id);
            if (constant == null) {
                constant = createConstant(id);
                // constant will self register in the constructor
            }
        } finally {
            lock.unlock();
        }

        return constant;
    }

    int register(T constant) {
        try {
            lock.lock();
            String id = constant.id();
            String name = constant.getClass().getSimpleName();

            if (mappings.containsKey(id)) {
                throw new IllegalStateException("Constant " + name + ":" + id + " is already registered");
            }

            int ordinal = mappings.size();
            ensureCapacity(ordinal);
            values.set(ordinal, constant);
            mappings.put(id, constant);
            return ordinal;
        } finally {
            lock.unlock();
        }
    }

    private void ensureCapacity(int index) {
        while (values.size() <= index) {
            values.add(null);
        }
    }

    @SneakyThrows
    private T createConstant(String id) {
        Constructor<T> constructor = type.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        return constructor.newInstance(id);
    }

    public int size() {
        return values.size();
    }

    public Collection<T> values() {
        return values;
    }
}
