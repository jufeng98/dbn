package com.dbn.common.clipboard;

import java.awt.datatransfer.DataFlavor;
import java.util.Objects;

public class HtmlContent extends ClipboardContent {

    public HtmlContent(String htmlText) {
        super(htmlText);
    }

    @Override
    protected DataFlavor[] createDataFlavors() throws Exception {
        DataFlavor[] dataFlavors = new DataFlavor[3];
        dataFlavors[0] = new DataFlavor("text/html;class=java.lang.String");
        dataFlavors[1] = new DataFlavor("text/rtf;class=java.lang.String");
        dataFlavors[2] = new DataFlavor("text/plain;class=java.lang.String");
        return dataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        String mimeType = flavor.getMimeType();
        return
            Objects.equals(mimeType, "text/html") ||
            Objects.equals(mimeType, "text/rtf") ||
            Objects.equals(mimeType, "text/plain");
    }
}
