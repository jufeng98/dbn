package com.dbn.common.constant;

import com.dbn.common.util.Commons;
import com.dbn.common.util.Strings;
import com.dbn.common.util.Unsafe;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.dbn.common.util.Commons.nvl;

final class PseudoConstantRegistry {
    private static final Map<Class<? extends PseudoConstant>, PseudoConstantData> REGISTRY = new ConcurrentHashMap<>();

    private PseudoConstantRegistry() {}

    static <T extends PseudoConstant<T>> PseudoConstantData<T> get(Class<T> clazz) {
		return Unsafe.cast(Commons.nvl(PseudoConstantData.LOCAL.get(), () -> REGISTRY.computeIfAbsent(clazz, c -> new PseudoConstantData<>(clazz))));
    }

    static <T extends PseudoConstant<T>> T get(Class<T> clazz, String id) {
        if (Strings.isEmpty(id)) return null;
        PseudoConstantData<T> data = get(clazz);
        return data.get(id);
    }

    public static <T extends PseudoConstant<T>> int register(T constant) {
        Class<T> clazz = Unsafe.cast(constant.getClass());
        PseudoConstantData<T> data = get(clazz);
        return data.register(constant);
    }

    static <T extends PseudoConstant<T>> T[] values(Class<T> clazz) {
        PseudoConstantData<T> data = get(clazz);
        return toArray(data.values(), clazz);
    }

    static <T extends PseudoConstant<T>> T[] list(Class<T> clazz, String csvIds) {
        if (Strings.isEmpty(csvIds)) return toArray(Collections.emptyList(), clazz);

        List<T> constants = new ArrayList<>();
        String[] ids = csvIds.split(",");

        for (String id : ids) {
            if (Strings.isNotEmpty(id)) {
                T constant = get(clazz, id.trim());
                constants.add(constant);
            }
        }
        return toArray(constants, clazz);
    }

    private static <T extends PseudoConstant<T>> T[] toArray(Collection<T> constants, Class<T> clazz) {
        return constants.toArray((T[]) Array.newInstance(clazz, constants.size()));
    }
}
