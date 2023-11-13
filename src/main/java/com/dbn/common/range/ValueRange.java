package com.dbn.common.range;

import lombok.Getter;

@Getter
public class ValueRange<T> extends Range{
    private final T value;

    private ValueRange(T value, int left, int right) {
        super(left, right);
        this.value = value;
    }

    public static <T> ValueRange<T> create(T value, int left, int right) {
        return new ValueRange<>(value, left, right);
    }

    @Override
    public String toString() {
        return value + " " + getLeft() + " - " + getRight();
    }
}
