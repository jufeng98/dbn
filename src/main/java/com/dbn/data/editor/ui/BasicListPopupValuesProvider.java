package com.dbn.data.editor.ui;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BasicListPopupValuesProvider extends ListPopupValuesProviderBase {
    private List<String> values;

    public BasicListPopupValuesProvider(String description, List<String> values) {
        super(description);
        this.values = values;
    }
}
