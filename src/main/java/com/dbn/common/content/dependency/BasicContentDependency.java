package com.dbn.common.content.dependency;

import com.dbn.common.content.DynamicContent;
import com.dbn.common.content.VoidDynamicContent;
import com.dbn.common.dispose.Failsafe;
import org.jetbrains.annotations.NotNull;

public class BasicContentDependency extends ContentDependency {
    private DynamicContent sourceContent;

    public BasicContentDependency(@NotNull DynamicContent sourceContent) {
        this.sourceContent = sourceContent;
        updateSignature();
    }

    @NotNull
    @Override
    public DynamicContent getSourceContent() {
        return Failsafe.nn(sourceContent);
    }

    @Override
    public void dispose() {
        sourceContent = VoidDynamicContent.INSTANCE;
    }
}
