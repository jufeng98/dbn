package com.dbn.editor.code.content;

import com.intellij.openapi.editor.RangeMarker;

import java.util.ArrayList;
import java.util.List;

public class GuardedBlockMarkers {
    private final List<GuardedBlockMarker> ranges = new ArrayList<>();

    public void addMarker(int startOffset, int endOffset) {
        ranges.removeIf(range -> range.getStartOffset() >= startOffset && range.getEndOffset() <= endOffset);
        ranges.add(new GuardedBlockMarker(startOffset, endOffset));
    }

    public List<GuardedBlockMarker> getRanges() {
        return ranges;
    }

    public void apply(List<RangeMarker> rangeMarkers) {
        reset();
        for (RangeMarker rangeMarker : rangeMarkers) {
            addMarker(
                rangeMarker.getStartOffset(),
                rangeMarker.getEndOffset());
        }
    }

    public void reset() {
        ranges.clear();
    }

    public boolean isEmpty() {
        return ranges.isEmpty();
    }
}
