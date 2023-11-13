package com.dbn.object.type;

import com.dbn.common.constant.Constant;

public enum DBTriggerEvent implements Constant<DBTriggerEvent> {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    TRUNCATE("truncate"),
    DROP("drop"),
    LOGON("logon"),
    ALTER("alter"),
    CREATE("create"),
    RENAME("rename"),
    DDL("ddl"),
    UNKNOWN("unknown");

    private String name;

    DBTriggerEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
