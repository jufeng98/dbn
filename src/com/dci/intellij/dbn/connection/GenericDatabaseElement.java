package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.common.dispose.Disposable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GenericDatabaseElement extends ConnectionProvider, Disposable {
    String getName();
    @NotNull Project getProject();
    @Nullable GenericDatabaseElement getParentElement();
    GenericDatabaseElement getUndisposedElement();

    @Nullable
    DynamicContent getDynamicContent(DynamicContentType dynamicContentType);

    @NotNull
    @Override
    default ConnectionHandler getConnectionHandler() {
        throw new UnsupportedOperationException();
    }
}
