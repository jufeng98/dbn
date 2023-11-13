package com.dbn.common.ui;

import com.dbn.common.routine.Consumer;
import lombok.Getter;

@Getter
public abstract class PresentableFactory<T extends Presentable> {
    private final String actionName;

    public PresentableFactory(String actionName) {
        this.actionName = actionName;
    }

    public abstract void create(Consumer<T> consumer);
}
