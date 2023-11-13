package com.dbn.diagnostics.data;

import lombok.Getter;

@Getter
public enum DiagnosticCategory {
    CONNECTION("Connection Diagnostics"),
    PARSER("Parser Diagnostics");

    private final String name;

    DiagnosticCategory(String name) {
        this.name = name;
    }
}
