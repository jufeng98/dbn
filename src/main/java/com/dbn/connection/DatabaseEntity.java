package com.dbn.connection;

import com.dbn.common.content.DynamicContent;
import com.dbn.common.content.DynamicContentType;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.ui.Presentable;
import com.dbn.common.util.Unsafe;
import com.dbn.connection.context.DatabaseContextBase;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBObjectBundle;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DatabaseEntity extends DatabaseContextBase, StatefulDisposable, Presentable {

    @NotNull
    default String getQualifiedName() {
        return getName();
    }

    @NotNull
    Project getProject();

    @Nullable
    default <E extends DatabaseEntity> E getParentEntity() {
        return null;
    }

    //@NotNull
    default <E extends DatabaseEntity> E ensureParentEntity() {
        return Failsafe.nn(getParentEntity());
    }

    @Nullable
    default <E extends DatabaseEntity> E getUndisposedEntity() {
        return Unsafe.cast(this);
    }

    @Nullable
    default DynamicContent<?> getDynamicContent(DynamicContentType<?> dynamicContentType) {
        return null;
    }

    default DynamicContentType<?> getDynamicContentType() {
        return null;
    }

    @NotNull
    @Override
    default ConnectionHandler getConnection() {
        throw new UnsupportedOperationException();
    };

    default boolean isObject() {
        return this instanceof DBObject;
    }

    default boolean isObjectBundle() {
        return this instanceof DBObjectBundle;
    }
}
