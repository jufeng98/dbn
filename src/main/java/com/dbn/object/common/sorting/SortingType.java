package com.dbn.object.common.sorting;

import com.dbn.common.ui.Presentable;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
public enum SortingType implements Presentable{
    NAME(nls("app.objectBrowser.const.SortingType_NAME")),
    POSITION(nls("app.objectBrowser.const.SortingType_POSITION"));

    private final String name;

    SortingType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
