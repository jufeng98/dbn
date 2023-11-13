package com.dbn.common.options.setting;

import com.dbn.common.util.Commons;
import com.intellij.openapi.options.ConfigurationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class Setting<T, E> {
    private final String name;
    private T value;

    protected Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public T value() {
        return value;
    }

    public boolean setValue(T value) {
        boolean response = !Commons.match(this.value, value);
        this.value = value;
        return response;
    }

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + "] " + name + " = " + value;
    }

    public abstract boolean to(E component) throws ConfigurationException;

    public abstract void from(E component);
}
