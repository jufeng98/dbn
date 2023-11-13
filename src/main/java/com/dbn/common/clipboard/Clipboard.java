package com.dbn.common.clipboard;

import com.intellij.openapi.ide.CopyPasteManager;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.DataFlavor;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class Clipboard {

    public static XmlContent createXmlContent(String text) {
        return new XmlContent(text);
    }

    public static HtmlContent createHtmlContent(String text) {
        return new HtmlContent(text);
    }

    @Nullable
    public static String getStringContent() {
        try {
            CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
            Object data = copyPasteManager.getContents(DataFlavor.stringFlavor);;
            if (data instanceof String) {
                return (String) data;
            }
        } catch (Throwable e) {
            conditionallyLog(e);
        }

        return null;
    }

}
