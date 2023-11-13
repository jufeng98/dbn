package com.dbn.language.common.psi;

public class LookupParameters {
    private boolean lenient;
    private SequencePsiElement sourceScope;

    public LookupParameters(boolean lenient, SequencePsiElement sourceScope) {
        this.lenient = lenient;
        this.sourceScope = sourceScope;
    }

    public boolean isLenient() {
        return lenient;
    }

    public SequencePsiElement getSourceScope() {
        return sourceScope;
    }
}
