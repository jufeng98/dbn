package com.dbn.common.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@UtilityClass
public class CollectionUtil {
    public static <T extends Cloneable<T>> void cloneElements(Collection<T> source, Collection<T> target) {
        for (T cloneable : source) {
            T clone = cloneable.clone();
            target.add(clone);
        }
    }

    public static void clear(Collection<?> collection) {
        if (collection != null && collection.size() > 0) {
            try {
                collection.clear();
            } catch (UnsupportedOperationException ignore) {}
        }
    }

    public static void clear(@Nullable Map<?, ?> map) {
        if (map != null) {
            try {
                map.clear();
            } catch (UnsupportedOperationException ignore) {}
        }
    }


    @NotNull
    @Contract(value = " -> new", pure = true)
    public static <T> List<T> createConcurrentList() {
        return new CopyOnWriteArrayList<>();
    }

}
