package com.dbn.common.file;

import com.dbn.common.dispose.Disposer;
import com.dbn.common.ref.WeakRefCache;
import com.dbn.common.routine.ParametricRunnable;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.vfs.AsyncFileListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileMoveEvent;
import com.intellij.openapi.vfs.newvfs.events.VFilePropertyChangeEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.dbn.common.file.util.VirtualFiles.*;
import static com.dbn.common.util.Lists.anyMatch;

@Setter
public class FileMappings<T> implements Disposable {
    private final Map<String, T> mappings = new ConcurrentHashMap<>();
    private final Set<BiPredicate<String, T>> verifiers = new HashSet<>();
    private final WeakRefCache<T, List<String>> urlCache = WeakRefCache.weakKey();
    private final WeakRefCache<T, List<VirtualFile>> fileCache = WeakRefCache.weakKey();
    private ParametricRunnable<VirtualFile, Throwable> fileChangeHandler;

    public FileMappings(@Nullable Disposable parentDisposable) {
        VirtualFileManager fileManager = VirtualFileManager.getInstance();
        fileManager.addAsyncFileListener(createFileListener(), this);

        addVerifier((f, v) -> isValidFile(f));
        Disposer.register(parentDisposable, this);
    }

    @NotNull
    private AsyncFileListener createFileListener() {
        return events -> {
            events.forEach(e -> handleFileEvent(e));
            return null;
        };
    }

    private void handleFileEvent(VFileEvent event) {
        VirtualFile file = event.getFile();
        if (file == null) return;
        if (!isLocalFileSystem(file)) return;

        if (event instanceof VFileDeleteEvent) {
            VFileDeleteEvent deleteEvent = (VFileDeleteEvent) event;
            remove(deleteEvent.getFile().getUrl());

        } else if (event instanceof VFileMoveEvent) {
            VFileMoveEvent moveEvent = (VFileMoveEvent) event;
            updateMapping(file,
                    moveEvent.getOldPath(),
                    moveEvent.getNewPath());

        } else if (event instanceof VFilePropertyChangeEvent) {
            VFilePropertyChangeEvent propertyChangeEvent = (VFilePropertyChangeEvent) event;
            updateMapping(file,
                    propertyChangeEvent.getOldPath(),
                    propertyChangeEvent.getNewPath());
        }
    }

    @SneakyThrows
    private void updateMapping(VirtualFile file, String oldPath, String newPath) {
        if (Objects.equals(oldPath, newPath)) return;

        String protocol = file.getFileSystem().getProtocol();
        String oldUrl = VirtualFileManager.constructUrl(protocol, oldPath);
        String newUrl = VirtualFileManager.constructUrl(protocol, newPath);

        T value = remove(oldUrl);
        if (value == null) return;

        put(newUrl, value);
        if (fileChangeHandler != null) fileChangeHandler.run(file);
    }

    public void addVerifier(BiPredicate<String, T> verifier) {
        verifiers.add(verifier);
    }

    public void removeIf(Predicate<T> condition) {
        mappings
                .entrySet()
                .stream()
                .filter(e -> condition.test(e.getValue()))
                .map(m -> m.getKey())
                .forEach(k -> remove(k));
    }

    public T get(@NotNull String fileUrl) {
        return mappings.get(fileUrl);
    }

    public T remove(String fileUrl) {
        T removed = mappings.remove(fileUrl);
        if (removed == null) return null;

        urlCache.clear();
        fileCache.clear();
        return removed;
    }

    public boolean contains(T value) {
        return anyMatch(mappings.values(), o -> Objects.equals(value, o));
    }

    public void put(@NotNull String url, T value) {
        mappings.put(url, value);
        urlCache.clear();
        fileCache.clear();
    }

    public Set<String> fileUrls() {
        return mappings.keySet();
    }

    public List<String> fileUrls(T value) {
        return urlCache.computeIfAbsent(value, v ->
                mappings.entrySet()
                        .stream()
                        .filter(e -> Objects.equals(e.getValue(), v))
                        .map(e -> e.getKey())
                        .collect(Collectors.toList()));
    }

    public List<VirtualFile> files(T value) {
        return fileCache.computeIfAbsent(value, v ->
                fileUrls(value)
                        .stream()
                        .map(u -> findFileByUrl(u))
                        .filter(f -> f != null)
                        .collect(Collectors.toList()));
    }

    public void cleanup() {
        for (BiPredicate<String, T> verifier : verifiers) {
            mappings.keySet().removeIf(url -> {
                T value = mappings.get(url);
                return !verifier.test(url, value);
            });
        }
        urlCache.clear();
        fileCache.clear();
    }

    public void clear() {
        mappings.clear();
        urlCache.clear();
        fileCache.clear();
    }

    public Collection<T> values() {
        return mappings.values();
    }

    @Override
    public void dispose() {
        clear();
        verifiers.clear();
    }
}
