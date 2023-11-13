package com.dbn.connection;

import com.dbn.common.constant.Constant;
import com.dbn.common.ui.Presentable;
import lombok.Getter;

@Getter
public enum AuthenticationType implements Constant<AuthenticationType>, Presentable {
    NONE("None"),
    USER("User"),
    USER_PASSWORD("User / Password"),
    OS_CREDENTIALS("OS Credentials");

    private final String name;

    AuthenticationType(String name) {
        this.name = name;
    }
}
