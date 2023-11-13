package com.dbn.language.common.element.impl;

import lombok.Getter;

import java.util.Objects;

@Getter
public class WrappingDefinition {
    private final TokenElementType beginElementType;
    private final TokenElementType endElementType;

    public WrappingDefinition(TokenElementType beginElementType, TokenElementType endElementType) {
        this.beginElementType = beginElementType;
        this.endElementType = endElementType;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (obj instanceof WrappingDefinition) {
            WrappingDefinition definition = (WrappingDefinition) obj;
            return
                Objects.equals(this.beginElementType.getTokenType(), definition.beginElementType.getTokenType()) &&
                Objects.equals(this.endElementType.getTokenType(), definition.endElementType.getTokenType());
        }
        return false;
    }
}
