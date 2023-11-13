package com.dbn.language.common;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum TokenTypeCategory {
    UNKNOWN("unknown"),
    KEYWORD("keyword"),
    FUNCTION("function"),
    PARAMETER("parameter"),
    DATATYPE("datatype"),
    OBJECT("object"),
    EXCEPTION("exception"),
    OPERATOR("operator"),
    CHARACTER("character"),
    IDENTIFIER("identifier"),
    CHAMELEON("chameleon"),
    WHITESPACE("whitespace"),
    COMMENT("comment"),
    NUMERIC("numeric"),
    LITERAL("literal")
    ;

    private final String name;

    TokenTypeCategory(String name) {
        this.name = name;
    }

    public static TokenTypeCategory getCategory(String categoryName) {
        for (TokenTypeCategory identifier : TokenTypeCategory.values()) {
            if (Objects.equals(identifier.name, categoryName)) return identifier;
        }
        return UNKNOWN;
    }
}
