package com.dbn.common.range;

import lombok.Getter;

@Getter
public class Range {
    private final int left;
    private final int right;

    public Range(int left, int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return getLeft() + " - " + getRight();
    }
}
