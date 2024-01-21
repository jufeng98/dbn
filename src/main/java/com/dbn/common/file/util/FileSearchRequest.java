package com.dbn.common.file.util;

import com.dbn.common.util.Strings;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Arrays;

public class FileSearchRequest {
    private final String[] names;
    private final String[] patterns;
    private final String[] extensions;

    private FileSearchRequest(String[] names, String[] patterns, String[] extensions) {
        this.names = names;
        this.patterns = patterns;
        this.extensions = extensions;
    }

    public static FileSearchRequest forNames(String ... names) {
        return new FileSearchRequest(names, null, null);
    }

    public static FileSearchRequest forPatterns(String ... patterns) {
        return new FileSearchRequest(null, patterns, null);
    }

    public static FileSearchRequest forExtensions(String ... extensions) {
        return new FileSearchRequest(null, null, extensions);
    }

    public boolean matches(VirtualFile file) {
        if (names != null) {
            return Arrays.stream(names).anyMatch(name -> Strings.equalsIgnoreCase(file.getName(), name));
        } else if (patterns != null) {
            return Arrays.stream(patterns).anyMatch(pattern -> file.getName().matches(pattern));
        } else if (extensions != null) {
            return Arrays.stream(extensions).anyMatch(extension -> Strings.equalsIgnoreCase(file.getExtension(), extension));
        }
        return false;
    }
}
