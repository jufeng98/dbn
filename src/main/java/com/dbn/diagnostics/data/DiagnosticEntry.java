package com.dbn.diagnostics.data;

public interface DiagnosticEntry<T> {
    String DEFAULT_QUALIFIER = "DEFAULT";

    T getIdentifier();

    String getQualifier();

    boolean isComposite();

    DiagnosticEntry<T> getDetail(String qualifier);

    long getInvocations();

    long getFailures();

    long getTimeouts();

    long getTotal();

    long getBest();

    long getWorst();

    long getAverage();

    void log(boolean failure, boolean timeout, long value);

    class Delegate<T> implements DiagnosticEntry<T> {

        @lombok.experimental.Delegate
        private DiagnosticEntry<T> getDefaultDetail() {
            return getDetail(DEFAULT_QUALIFIER);
        }
    }
}
