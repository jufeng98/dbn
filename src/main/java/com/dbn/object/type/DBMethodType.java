package com.dbn.object.type;

import com.dbn.common.constant.Constant;

public enum DBMethodType implements Constant<DBMethodType> {
    FUNCTION("function"),
    PROCEDURE("procedure");

    private String name;

    DBMethodType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
