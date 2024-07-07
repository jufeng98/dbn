package com.dbn.object;

public interface DBView extends DBDataset {
    String getViewComment();

    DBType getType();

    boolean isSystemView();
}