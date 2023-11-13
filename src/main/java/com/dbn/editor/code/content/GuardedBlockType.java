package com.dbn.editor.code.content;

import com.intellij.openapi.util.Key;

public enum GuardedBlockType {
    READONLY_DOCUMENT,
    READONLY_DOCUMENT_SECTION;

    public static final Key<GuardedBlockType> KEY = new Key<>("GUARDED_BLOCK_TYPE");
}
