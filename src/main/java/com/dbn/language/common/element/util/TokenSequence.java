package com.dbn.language.common.element.util;


import com.dbn.language.common.element.impl.TokenElementType;

import java.util.ArrayList;
import java.util.List;

public class TokenSequence {
    private List<TokenElementType> elements = new ArrayList<>();

    public void createVariant(TokenElementType additionalElement) {
        TokenSequence sequence = new TokenSequence();
        sequence.elements.addAll(elements);
        sequence.elements.add(additionalElement);
    }
}
