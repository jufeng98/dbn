package com.dbn.object.common.operation;

public enum DBOperationType {

    ENABLE("Enable"),
    DISABLE("Disable");


    private String name;

    DBOperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
