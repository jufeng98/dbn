package com.dbn.connection;

import com.dbn.common.constant.Constant;
import com.dbn.common.ui.Presentable;
import lombok.Getter;

@Getter
public enum DatabaseUrlType implements Presentable, Constant<DatabaseUrlType> {
    TNS("TNS"),
    SID("SID"),
    SERVICE("Service name"),
    LDAP("LDAP"),
    LDAPS("LDAP over SSL"),
    DATABASE("Database"),
    CUSTOM("Custom"),
    FILE("File");

    private final String name;

    DatabaseUrlType(String name) {
        this.name = name;
    }

}
