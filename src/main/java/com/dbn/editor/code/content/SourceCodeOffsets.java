package com.dbn.editor.code.content;

import com.intellij.openapi.editor.RangeMarker;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SourceCodeOffsets {
    private final GuardedBlockMarkers guardedBlocks = new GuardedBlockMarkers();
    private int headerEndOffset = 0;

    public void addGuardedBlock(int startOffset, int endOffset) {
        guardedBlocks.addMarker(startOffset, endOffset);
    }

    public void setGuardedBlocks(List<RangeMarker> rangeMarkers) {
        this.guardedBlocks.apply(rangeMarkers);
    }
}
