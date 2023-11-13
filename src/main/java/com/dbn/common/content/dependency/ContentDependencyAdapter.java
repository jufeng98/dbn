package com.dbn.common.content.dependency;

import com.intellij.openapi.Disposable;

public interface ContentDependencyAdapter extends Disposable {

    default boolean isDependencyDirty() {
        return false;
    }

    /**
     * This method is typically called when the dynamic content is dirty and
     * the system tries to reload it.
     * e.g. one basic condition for reloading dirty content is valid connectivity
     */
    default boolean canLoad() {
        return true;
    }

    default boolean canLoadInBackground() {
        return true;
    }


    void refreshSources();

    /**
     * This operation is triggered before loading the dynamic content is started.
     * It can be implemented by the adapters to load non-weak dependencies for example.
     * @param force load flavor
     */
    default void beforeLoad(boolean force) {};

    /**
     * This operation is triggered after the loading of the dynamic content.
     */
    default void afterLoad() {};


    boolean canLoadFast();

}
