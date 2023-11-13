package com.dbn.data.editor.ui;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public abstract class ListPopupValuesProviderBase implements ListPopupValuesProvider{
    private final String description;
    private boolean loaded = true;

    public ListPopupValuesProviderBase(String description) {
        this.description = description;
    }

    public ListPopupValuesProviderBase(String description, boolean loaded) {
        this.description = description;
        this.loaded = loaded;
    }

    @Override
    public List<String> getSecondaryValues() {
        return Collections.emptyList();
    }
}
