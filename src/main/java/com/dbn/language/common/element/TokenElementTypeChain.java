package com.dbn.language.common.element;


import com.dbn.language.common.element.impl.TokenElementType;

import java.util.ArrayList;
import java.util.List;

public class TokenElementTypeChain {
    private List<TokenElementType> elementTypes = new ArrayList<>();

    private TokenElementTypeChain(){};

    public TokenElementTypeChain(TokenElementType first) {
        elementTypes.add(first);
    }

    public void append(TokenElementType tokenElementType) {
        elementTypes.add(tokenElementType);
    }

    public List<TokenElementType> getElementTypes() {
        return elementTypes;
    }

    public TokenElementTypeChain createVariant(TokenElementType tokenElementType) {
        TokenElementTypeChain variant = new TokenElementTypeChain();
        variant.elementTypes.addAll(elementTypes);
        variant.elementTypes.add(tokenElementType);
        return variant;
    }

    @Override
    public String toString() {
        return elementTypes.toString();
    }
}
