package com.dbn.editor.data;

import com.dbn.common.property.PropertyHolderBase;

public class DatasetEditorStatusHolder extends PropertyHolderBase.IntStore<DatasetEditorStatus> {

    @Override
    protected DatasetEditorStatus[] properties() {
        return DatasetEditorStatus.VALUES;
    }
}
