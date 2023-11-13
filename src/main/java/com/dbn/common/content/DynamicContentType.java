package com.dbn.common.content;

import com.dbn.common.constant.Constant;
import org.jetbrains.annotations.NotNull;

public interface DynamicContentType<T extends DynamicContentType<T>> extends Constant<T> {
    default boolean matches(T contentType) {
        return false;
    }

    default T getGenericType() {
        return null;
    };

    DynamicContentType NULL = new DynamicContentType() {
        @Override
        public boolean matches(DynamicContentType contentType) {
            return contentType == this;
        }

        @Override
        public String toString() {
            return "NULL";
        }

        @Override
        public int ordinal() {
            return 0;
        }

        @Override
        public int compareTo(@NotNull Object o) {
            return 0;
        }
    };
}
