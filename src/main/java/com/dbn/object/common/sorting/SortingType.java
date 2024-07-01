package com.dbn.object.common.sorting;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum SortingType implements Presentable{
    NAME(nls("app.objects.const.SortingType_NAME")),
    POSITION(nls("app.objects.const.SortingType_POSITION"));

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
