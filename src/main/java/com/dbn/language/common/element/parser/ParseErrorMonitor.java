package com.dbn.language.common.element.parser;

import java.util.HashSet;
import java.util.Set;

public class ParseErrorMonitor {
    private final ParserBuilder builder;
    private final Set<Integer> errorOffsets = new HashSet<>();

    public ParseErrorMonitor(ParserBuilder builder) {
        this.builder = builder;
    }

    boolean isErrorAtOffset() {
        return isErrorAt(builder.getOffset());
    }

    boolean isErrorAt(int offset) {
        return errorOffsets.contains(offset);
    }

    void markError() {
        errorOffsets.add(builder.getOffset());
    }

    public void reset() {
        errorOffsets.removeIf(offset -> offset >= builder.getOffset());
    }
}
