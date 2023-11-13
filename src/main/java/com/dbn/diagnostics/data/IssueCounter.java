package com.dbn.diagnostics.data;

import lombok.Getter;

@Getter
public class IssueCounter {
    public static final IssueCounter EMPTY = new IssueCounter() {
        @Override
        public void merge(int errors, int warnings) {
            throw new UnsupportedOperationException("Immutable");
        }
    };

    private int errors;
    private int warnings;

    public IssueCounter() {}

    public IssueCounter(int errors, int warnings) {
        this.errors = errors;
        this.warnings = warnings;
    }

    public void merge(int errors, int warnings) {
        this.errors += errors;
        this.warnings += warnings;
    }

    public int issueCount() {
        return errors + warnings;
    }
}
