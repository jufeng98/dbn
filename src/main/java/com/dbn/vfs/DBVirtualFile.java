package com.dbn.vfs;

import com.dbn.common.environment.EnvironmentTypeProvider;
import com.dbn.connection.context.DatabaseContextBase;
import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface DBVirtualFile extends /*VirtualFileWithId, */EnvironmentTypeProvider, DatabaseContextBase, UserDataHolder {
    @NotNull
    Project getProject();

    Icon getIcon();

    void setCachedViewProvider(@Nullable DatabaseFileViewProvider viewProvider);

    @Nullable
    DatabaseFileViewProvider getCachedViewProvider();

    void invalidate();

    @Nullable
    default DBObjectRef getObjectRef() {
        return null;
    }

    @Nullable
    default DBObject getObject() {
        return null;
    }
}