package com.dbn.common.clipboard;

import java.awt.datatransfer.DataFlavor;
import java.util.Objects;

public class JiraContent extends ClipboardContent {

    public JiraContent(String markupText) {
        super(markupText);
    }

    @Override
    protected DataFlavor[] createDataFlavors() throws Exception {
        DataFlavor[] dataFlavors = new DataFlavor[1];
        dataFlavors[0] = new DataFlavor("text/plain;class=java.lang.String");
        return dataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return Objects.equals(flavor.getMimeType(), "text/plain");
    }
}
