package com.dbn.common.content.dependency;

public class BasicDependencyAdapter implements ContentDependencyAdapter {
    public static BasicDependencyAdapter REGULAR = new BasicDependencyAdapter();

    public static BasicDependencyAdapter FAST = new BasicDependencyAdapter() {
        @Override
        public boolean canLoadFast() {
            return true;
        }
    };

    @Override
    public void refreshSources() {

    }

    @Override
    public boolean canLoadFast() {
        return false;
    }

    @Override
    public void dispose() {
    }
}
