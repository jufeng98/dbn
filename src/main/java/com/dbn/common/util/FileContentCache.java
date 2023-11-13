package com.dbn.common.util;

import com.dbn.common.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public abstract class FileContentCache<T> {
    private final Map<File, Pair<T, Long>> cache = new ConcurrentHashMap<>();

    public final T get(File file) {
        return cache.compute(file, (f, v) -> {
            long timestamp = lastModified(f);
            if (v != null && v.second() == timestamp) return v;
            return Pair.of(load(f), timestamp);
        }).first();
    }

    protected abstract T load(File f);

    private static long lastModified(File file) {
        try {
            Path filePath = file.toPath();
            BasicFileAttributes fileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);
            return fileAttributes.lastModifiedTime().toMillis();
        } catch (Exception e) {
            conditionallyLog(e);
            return 0;
        }
    }
}
