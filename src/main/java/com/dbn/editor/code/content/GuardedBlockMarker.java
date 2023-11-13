package com.dbn.editor.code.content;

import com.intellij.util.Range;
import org.jetbrains.annotations.NotNull;

public class GuardedBlockMarker extends Range<Integer> implements Comparable<GuardedBlockMarker>{
    public static final String START_OFFSET_IDENTIFIER = "$$DBN_GUARDED_BLOCK_START_OFFSET$$";
    public static final String END_OFFSET_IDENTIFIER = "$$DBN_GUARDED_BLOCK_END_OFFSET$$";

    public GuardedBlockMarker(int startOffset, int endOffset) {
        super(startOffset, endOffset);
    }

    public int getStartOffset() {
        return getFrom();
    }

    public int getEndOffset() {
        return getTo();
    }

    @Override
    public int compareTo(@NotNull GuardedBlockMarker o) {
        return getStartOffset() - o.getStartOffset();
    }
}
