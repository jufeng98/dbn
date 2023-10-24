package com.dci.intellij.dbn.common.file.util;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

class FileCollector extends VirtualFileVisitor {
    private final FileTypeManager fileTypeManager = FileTypeManager.getInstance();
    private final Map<String, VirtualFile> bucket = new HashMap<>();
    private final FileSearchRequest request;

    private FileCollector(FileSearchRequest request) {
        this.request = request;
    }

    public static FileCollector create(FileSearchRequest request) {
        return new FileCollector(request);
    }

    public boolean visitFile(@NotNull VirtualFile file) {
        boolean fileIgnored = fileTypeManager.isFileIgnored(file.getName());
        if (!fileIgnored) {
            if (file.isDirectory()) {
                return true;
            } else {
                if (request.matches(file)) {
                    bucket.put(file.getPath(), file);
                }
                return false;
            }
        }
        return false;
    }

    public VirtualFile[] files() {
        return bucket.values().toArray(new VirtualFile[0]);
    }
}
