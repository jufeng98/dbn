package com.dbn.editor.data;

import com.dbn.common.property.Property;

public enum DatasetEditorStatus implements Property.IntBase {
    CONNECTED,
    LOADING,
    LOADED;

    public static final DatasetEditorStatus[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);

    @Override
    public IntMasks masks() {
        return masks;
    }
}
