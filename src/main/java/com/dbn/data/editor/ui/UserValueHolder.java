package com.dbn.data.editor.ui;

import com.dbn.connection.ConnectionHandler;
import com.dbn.data.editor.text.TextContentType;
import com.dbn.data.type.DBDataType;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface UserValueHolder<T> {
    void setUserValue(T userValue);
    void updateUserValue(T userValue, boolean bulk);
    TextContentType getContentType();
    void setContentType(TextContentType contentType);
    T getUserValue();
    String getPresentableValue();
    String getName();
    DBDataType getDataType();
    DBObjectType getObjectType();
    Project getProject();

    @NotNull
    default ConnectionHandler getConnection() {
        throw new UnsupportedOperationException();
    }
}
