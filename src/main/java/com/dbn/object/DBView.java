package com.dbn.object;

public interface DBView extends DBDataset {
    DBType getType();

    boolean isSystemView();
}