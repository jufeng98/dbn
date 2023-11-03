package com.dci.intellij.dbn.common.component;

import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import com.intellij.openapi.components.NamedComponent;
import com.intellij.openapi.project.DumbAware;

public interface Service extends DumbAware, NamedComponent, StatefulDisposable {

    @Override
    default void disposeInner() {
        nullify();
    }

}
