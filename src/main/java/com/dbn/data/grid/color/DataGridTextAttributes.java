package com.dbn.data.grid.color;

import com.intellij.ui.SimpleTextAttributes;

import java.awt.*;

public interface DataGridTextAttributes {
    SimpleTextAttributes getSelection();

    SimpleTextAttributes getSearchResult();

    Color getCaretRowBgColor();

    SimpleTextAttributes getPlainData(boolean modified, boolean atCaretRow);

    SimpleTextAttributes getLoadingData(boolean atCaretRow);
}
