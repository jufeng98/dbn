package com.dbn.common.content.dependency;

import com.dbn.common.content.DynamicContent;
import com.dbn.common.content.DynamicContentType;
import com.dbn.common.content.VoidDynamicContent;
import com.dbn.connection.DatabaseEntity;
import org.jetbrains.annotations.NotNull;

public class LinkedContentDependency extends ContentDependency {
    private DatabaseEntity sourceContentOwner;
    private final DynamicContentType sourceContentType;

    public LinkedContentDependency(@NotNull DatabaseEntity sourceContentOwner, @NotNull DynamicContentType sourceContentType) {
        this.sourceContentOwner = sourceContentOwner;
        this.sourceContentType = sourceContentType;
        updateSignature();
    }

    @Override
    @NotNull
    public DynamicContent getSourceContent() {
        if (sourceContentOwner == null) return VoidDynamicContent.INSTANCE;

        DynamicContent sourceContent = sourceContentOwner.getDynamicContent(sourceContentType);
        if (sourceContent != null) return sourceContent;

        return VoidDynamicContent.INSTANCE;
    }

    @Override
    public void dispose() {
        sourceContentOwner = null;
    }
}
