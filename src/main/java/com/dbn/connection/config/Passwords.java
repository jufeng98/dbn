package com.dbn.connection.config;

import com.dbn.common.util.Strings;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
@UtilityClass
public final class Passwords {

    public static String encodePassword(String password) {
        try {
            password = Strings.isEmpty(password) ? "" : new String(Base64.getEncoder().encode(nvl(password).getBytes()));
        } catch (Exception e) {
            conditionallyLog(e);
            // any exception would break the logic storing the connection settings
            log.error("Error encoding password", e);
        }
        return password;
    }

    public static String decodePassword(String password) {
        try {
            password = Strings.isEmpty(password) ? "" : new String(Base64.getDecoder().decode(nvl(password).getBytes()));
        } catch (Exception e) {
            conditionallyLog(e);
            // password may not be encoded yet
        }

        return password;
    }

    private static String nvl(String value) {
        return value == null ? "" : value;
    }

}
