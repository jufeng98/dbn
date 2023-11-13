package com.dbn.common.content;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.ui.Presentable;

public interface DynamicContentElement extends StatefulDisposable, Comparable, Presentable {

    default short getOverload() { return 0; }
    default void reload() {}
    default void refresh() {}
    DynamicContentType getDynamicContentType();
}
