package com.dbn.common.constant;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PseudoConstantConverter<T extends PseudoConstant> extends Converter<T> {
    private final Class<T> typeClass;

    public PseudoConstantConverter(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    @Nullable
    @Override
    public T fromString(@NotNull String value) {
        return (T) PseudoConstant.get(typeClass, value);
    }

    @NotNull
    @Override
    public String toString(@NotNull T constant) {
        return constant.id();
    }
}
