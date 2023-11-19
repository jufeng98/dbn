package com.dbn.common.util;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@UtilityClass
public class Characters {
    private static final Map<Character, Character> UPPER_CASE_CHARS = new ConcurrentHashMap<>();
    private static final Map<Character, Character> LOWER_CASE_CHARS = new ConcurrentHashMap<>();

    public static char toUpperCase(char chr){
        return UPPER_CASE_CHARS.computeIfAbsent(chr, c -> Strings.toUpperCase(String.valueOf(c)).charAt(0));
    }

    public static char toLowerCase(char chr){
        return LOWER_CASE_CHARS.computeIfAbsent(chr, c -> Strings.toLowerCase(String.valueOf(c)).charAt(0));
    }

    public static boolean equalIgnoreCase(char char1, char char2) {
        return Objects.equals(toUpperCase(char1), toLowerCase(char2));
    }

}
