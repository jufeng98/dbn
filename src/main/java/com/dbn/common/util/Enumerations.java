package com.dbn.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Enumerations {

    @SafeVarargs
    public static <T extends Enum<T>> boolean isOneOf(Enum<T> enumeration, Enum<T> ... values) {
        if (values != null && values.length > 0) {
            for (Enum<T> value : values) {
                if (value == enumeration) return true;
            }
        }
        return false;
    }
}
