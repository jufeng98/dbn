package com.dbn.common.clipboard;

import lombok.extern.slf4j.Slf4j;

import java.awt.datatransfer.DataFlavor;
import java.util.Objects;

@Slf4j
public class XmlContent extends ClipboardContent {

    public XmlContent(String text) {
        super(text);
    }

    @Override
    protected DataFlavor[] createDataFlavors() throws Exception {
        DataFlavor[] dataFlavors = new DataFlavor[4];
        dataFlavors[0] = new DataFlavor("text/xml;class=java.lang.String");
        dataFlavors[1] = new DataFlavor("text/rtf;class=java.lang.String");
        dataFlavors[2] = new DataFlavor("text/plain;class=java.lang.String");
        dataFlavors[3] = DataFlavor.stringFlavor;
        return dataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        String mimeType = flavor.getMimeType();
        return
            Objects.equals(mimeType, "text/xml") ||
            Objects.equals(mimeType, "text/rtf") ||
            Objects.equals(mimeType, "text/plain");
    }
}
