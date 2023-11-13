package com.dbn.common.clipboard;

import lombok.extern.slf4j.Slf4j;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

@Slf4j
public abstract class ClipboardContent implements Transferable {
    private final DataFlavor[] dataFlavors;
    private final String content;

    public ClipboardContent(String content) {
        this.content = content;
        this.dataFlavors = createDataFlavorsGuarded();
    }

    protected DataFlavor[] createDataFlavorsGuarded() {
        try {
            return createDataFlavors();
        } catch (Throwable e) {
            log.warn("Failed to initialise data flavors for {}. Returning string flavor", getClass().getSimpleName(), e);
            DataFlavor[] dataFlavors = {DataFlavor.stringFlavor};
            return dataFlavors;
        }
    }

    protected abstract DataFlavor[] createDataFlavors() throws Exception;

    @Override
    public final DataFlavor[] getTransferDataFlavors() {
        return dataFlavors;
    }

    @Override
    public final Object getTransferData(DataFlavor flavor){
        return content;
    }
}
