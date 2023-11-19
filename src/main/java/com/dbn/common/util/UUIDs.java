package com.dbn.common.util;


import lombok.experimental.UtilityClass;

import java.util.UUID;

import static com.dbn.common.util.Strings.toUpperCase;

@UtilityClass
public class UUIDs {

/*
    public static String compact() {
        return RandomStringUtils.random(22, "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZ");
    }
*/

    public static String compact() {
        return toUpperCase(UUID.randomUUID().toString().replaceAll("-", ""));
    }

    public static String regular() {
        return UUID.randomUUID().toString();
    }
}
