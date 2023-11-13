package com.dbn.debugger.jdwp;

import org.jetbrains.annotations.NotNull;

public class DBJdwpSourcePath {
    private String signature;
    private String programType;
    private String programOwner;
    private String programName;

    private DBJdwpSourcePath(String sourceUrl) {
        String[] tokens;
        if (sourceUrl.contains("\\")) {
            tokens = sourceUrl.split("[\\\\.:]");
        } else if (sourceUrl.contains("/")) {
            tokens = sourceUrl.split("[/.:]");
        } else {
            tokens = sourceUrl.split("[.:]");
        }

        if (tokens.length < 4) {
            throw new UnsupportedOperationException("Cannot tokenize source path: " + sourceUrl);
        }
        signature = tokens[0];
        programType = tokens[1];
        programOwner = tokens[2];
        programName = tokens[3];
    }

    public static DBJdwpSourcePath from(@NotNull String sourceUrl) throws Exception {
        return new DBJdwpSourcePath(sourceUrl);
    }

    public String getSignature() {
        return signature;
    }

    public String getProgramType() {
        return programType;
    }

    public String getProgramOwner() {
        return programOwner;
    }

    public String getProgramName() {
        return programName;
    }
}
