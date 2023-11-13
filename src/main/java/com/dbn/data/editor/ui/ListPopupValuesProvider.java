package com.dbn.data.editor.ui;

import java.util.Collections;
import java.util.List;

public interface ListPopupValuesProvider {
    String getDescription();

    List<String> getValues();

    default List<String> getSecondaryValues() {
        return Collections.emptyList();
    }

    default boolean isLoaded() {
        return true;
    }

    default void setLoaded(boolean loaded) {};
}
