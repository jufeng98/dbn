package com.dbn.common.util;


import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDs {

/*
    public static String compact() {
        return RandomStringUtils.random(22, "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZ");
    }
*/

    public static String compact() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static String regular() {
        return UUID.randomUUID().toString();
    }
}
