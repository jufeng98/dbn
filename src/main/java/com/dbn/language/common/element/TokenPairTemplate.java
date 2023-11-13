package com.dbn.language.common.element;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum TokenPairTemplate {
    PARENTHESES("CHR_LEFT_PARENTHESIS", "CHR_RIGHT_PARENTHESIS", false),
    BRACKETS("CHR_LEFT_BRACKET", "CHR_RIGHT_BRACKET", false),
    BEGIN_END("KW_BEGIN", "KW_END", true);

    private final String beginToken;
    private final String endToken;
    private final boolean block;

    TokenPairTemplate(String beginToken, String endToken, boolean block) {
        this.beginToken = beginToken;
        this.endToken = endToken;
        this.block = block;
    }

    public static TokenPairTemplate get(String tokenTypeId) {
        for (TokenPairTemplate tokenPairTemplate : values()) {
            if (Objects.equals(tokenPairTemplate.beginToken, tokenTypeId) ||
                    Objects.equals(tokenPairTemplate.endToken, tokenTypeId)) {
                return tokenPairTemplate;
            }
        }
        return null;
    }
}
