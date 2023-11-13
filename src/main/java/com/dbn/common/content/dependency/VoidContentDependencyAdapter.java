package com.dbn.common.content.dependency;

public class VoidContentDependencyAdapter implements ContentDependencyAdapter{
    public static final VoidContentDependencyAdapter INSTANCE = new VoidContentDependencyAdapter();

    private VoidContentDependencyAdapter() {

    }

    @Override
    public boolean canLoad() {
        return false;
    }

    @Override
    public boolean isDependencyDirty() {
        return false;
    }

    @Override
    public void refreshSources() {

    }

    @Override
    public void beforeLoad(boolean force) {

    }

    @Override
    public void afterLoad() {

    }


    @Override
    public boolean canLoadFast() {
        return true;
    }

    @Override
    public void dispose() {

    }
}
