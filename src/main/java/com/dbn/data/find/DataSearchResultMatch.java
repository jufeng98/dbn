package com.dbn.data.find;

import com.dbn.data.model.DataModelCell;
import lombok.Getter;
import lombok.NonNull;

import static com.dbn.common.dispose.Failsafe.nd;

@Getter
public class DataSearchResultMatch {
    private final int startOffset;
    private final int endOffset;
    private final DataModelCell cell;

    public DataSearchResultMatch(DataModelCell cell, int startOffset, int endOffset) {
        this.cell = cell;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    @NonNull
    public DataModelCell getCell() {
        return nd(cell);
    }

    public int getColumnIndex() {
        return getCell().getIndex();
    }

    public int getRowIndex() {
        return getCell().getRow().getIndex();
    }
}
