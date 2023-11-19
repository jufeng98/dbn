package com.dbn.language.common.element.util;

import static com.dbn.common.util.Strings.cachedLowerCase;

public enum IdentifierType {
    OBJECT,
    ALIAS,
    VARIABLE,
    UNKNOWN;

    private final String lowerCaseName;

    IdentifierType() {
        this.lowerCaseName = cachedLowerCase(name());
    }

    public String lowerCaseName() {
        return lowerCaseName;
    }
}
