package com.dbn.common.util;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MathResult {
    private final BigDecimal sum;
    private final BigDecimal count;
    private final BigDecimal average;

    public MathResult(BigDecimal sum, BigDecimal count, BigDecimal average) {
        this.sum = sum;
        this.count = count;
        this.average = average;
    }
}
