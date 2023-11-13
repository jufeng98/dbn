package com.dbn.language.common.element.parser;

import lombok.Getter;

@Getter
public enum ParseResultType {
    FULL_MATCH (2),
    PARTIAL_MATCH(1),
    NO_MATCH(0);

    private final int score;

    ParseResultType(int score) {
        this.score = score;
    }

    public static ParseResultType worseOf(ParseResultType ... resultTypes) {
        ParseResultType worse = null;
        for (ParseResultType resultType : resultTypes) {
            if (worse == null || worse.score > resultType.score) {
                worse = resultType;
            }
        }
        return worse;
    }

}
