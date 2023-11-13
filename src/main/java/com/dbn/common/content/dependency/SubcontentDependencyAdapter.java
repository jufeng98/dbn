package com.dbn.common.content.dependency;

import com.dbn.common.content.DynamicContent;
import com.dbn.common.content.DynamicContentType;
import com.dbn.connection.DatabaseEntity;
import org.jetbrains.annotations.NotNull;

public interface SubcontentDependencyAdapter extends ContentDependencyAdapter{
    @NotNull
    DynamicContent getSourceContent();

    static SubcontentDependencyAdapter create(@NotNull DatabaseEntity sourceContentOwner, @NotNull DynamicContentType sourceContentType) {
        return new SubcontentDependencyAdapterImpl(sourceContentOwner, sourceContentType);
    }

}
