package com.dbn.common.cache;

import lombok.Value;

import java.util.Arrays;

@Value
public class CacheKey<T> {
    private final String[] path;
    private final String key;

    private CacheKey(String... tokens) {
        if (tokens.length == 1) {
            this.path = new String[]{"SHARED"};
        } else {
            this.path = Arrays.copyOf(tokens, tokens.length - 1);
        }
        this.key = tokens[tokens.length - 1];
    }

    public static <T> CacheKey<T> key(String... tokens) {
        return new CacheKey<>(tokens);
    }
}
