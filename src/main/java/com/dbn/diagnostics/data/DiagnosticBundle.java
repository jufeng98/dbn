package com.dbn.diagnostics.data;

import com.dbn.common.latent.Latent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DiagnosticBundle<T extends Comparable<T>> {
    private final DiagnosticType type;
    private final boolean composite;
    private final Map<T, DiagnosticEntry<T>> entries = new ConcurrentHashMap<>();
    private final Latent<List<T>> keys = Latent.mutable(
            () -> getSignature(),
            () -> new ArrayList<>(entries.keySet()));

    private transient int signature;

    private DiagnosticBundle(DiagnosticType type, boolean composite) {
        this.type = type;
        this.composite = composite;
    }

    public static <T extends Comparable<T>> DiagnosticBundle<T> basic(DiagnosticType type) {
        return new DiagnosticBundle<>(type, false);
    }

    public static <T extends Comparable<T>> DiagnosticBundle<T> composite(DiagnosticType type) {
        return new DiagnosticBundle<>(type, true);
    }

    public int getSignature() {
        return signature;
    }

    public int size() {
        return getKeys().size();
    }

    public List<T> getKeys() {
        return keys.get();
    }

    public DiagnosticEntry<T> get(T identifier) {
        return this.entries.computeIfAbsent(identifier, i -> createEntry(i));
    }
    public DiagnosticEntry<T> get(T key, String qualifier) {
        return get(key).getDetail(qualifier);
    }

    public DiagnosticEntry<T> log(T identifier, boolean failure, boolean timeout, long value) {
        return log(identifier, DiagnosticEntry.DEFAULT_QUALIFIER, failure, timeout, value);
    }

    public DiagnosticEntry<T> log(T identifier, String qualifier, boolean failure, boolean timeout, long value) {
        DiagnosticEntry<T> entry = get(identifier, qualifier);
        entry.log(failure, timeout, value);
        signature++;
        return entry;
    }


    private DiagnosticEntry<T> createEntry(T identifier) {
        return composite ?
                new DiagnosticEntryComposite<>(identifier) :
                new DiagnosticEntryBase<>(identifier);
    }

    @Override
    public String toString() {
        return type + " (" + size() + " entries)";
    }
}
