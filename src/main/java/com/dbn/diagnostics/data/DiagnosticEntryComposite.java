package com.dbn.diagnostics.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DiagnosticEntryComposite<T extends Comparable<T>> extends DiagnosticEntry.Delegate<T> {
    private final T identifier;
    private final Map<String, DiagnosticEntry<T>> details = new ConcurrentHashMap<>();

    public DiagnosticEntryComposite(T identifier) {
        this.identifier = identifier;
    }

    public DiagnosticEntry<T> getDetail(String qualifier) {
        return details.computeIfAbsent(qualifier, q -> new DiagnosticEntryBase<>(identifier));
    }
}
