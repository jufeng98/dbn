package com.dbn.common.content.dependency;

import com.dbn.common.content.DynamicContent;
import com.dbn.common.content.DynamicContentType;
import com.dbn.common.dispose.Disposer;
import com.dbn.connection.DatabaseEntity;
import org.jetbrains.annotations.NotNull;

class SubcontentDependencyAdapterImpl extends BasicDependencyAdapter implements SubcontentDependencyAdapter {
    private ContentDependency contentDependency;

    SubcontentDependencyAdapterImpl(@NotNull DatabaseEntity sourceContentOwner, @NotNull DynamicContentType sourceContentType) {
        contentDependency = new LinkedContentDependency(sourceContentOwner, sourceContentType);
    }


    @Override
    @NotNull
    public DynamicContent getSourceContent() {
        return contentDependency.getSourceContent();
    }

    @Override
    public boolean canLoad() {
        return getSourceContent().isLoaded();
    }

    @Override
    public boolean isDependencyDirty() {
        return contentDependency.isDirty();
    }

    @Override
    public void refreshSources() {
        DynamicContent sourceContent = getSourceContent();
        sourceContent.refresh();
    }

    @Override
    public void beforeLoad(boolean force) {
        if (!force) return;

        DynamicContent sourceContent = getSourceContent();
        sourceContent.refresh();
    }

    @Override
    public void afterLoad() {
        contentDependency.updateSignature();
    }

    @Override
    public boolean canLoadFast() {
        return getSourceContent().isReady();
    }

    @Override
    public boolean canLoadInBackground() {
        DynamicContent sourceContent = getSourceContent();
        if (sourceContent.isLoadingInBackground()) return false;

        ContentDependencyAdapter sourceDependencyAdapter = sourceContent.getDependencyAdapter();
        if (!sourceDependencyAdapter.canLoadInBackground()) return false;

        return true;
    }

    @Override
    public void dispose() {
        Disposer.dispose(contentDependency);
        contentDependency = VoidContentDependency.INSTANCE;
        super.dispose();
    }


}
