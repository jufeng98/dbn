package com.dbn.language.common.element.util;

public enum IdentifierType {
    OBJECT,
    ALIAS,
    VARIABLE,
    UNKNOWN;

    private final String lowerCaseName;

    IdentifierType() {
        this.lowerCaseName = name().toLowerCase();
    }

    public String lowerCaseName() {
        return lowerCaseName;
    }
}
