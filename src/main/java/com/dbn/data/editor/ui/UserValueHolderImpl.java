package com.dbn.data.editor.ui;

import com.dbn.common.project.ProjectRef;
import com.dbn.data.editor.text.TextContentType;
import com.dbn.data.type.DBDataType;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.project.Project;

public class UserValueHolderImpl<T> implements UserValueHolder<T>{
    private final String name;
    private final DBDataType dataType;
    private final DBObjectType objectType;
    private final ProjectRef project;
    private T userValue;
    private TextContentType contentType;

    public UserValueHolderImpl(String name, DBObjectType objectType, DBDataType dataType, Project project) {
        this.name = name;
        this.objectType = objectType;
        this.dataType = dataType;
        this.project = ProjectRef.of(project);
    }

    @Override
    public T getUserValue() {
        return userValue;
    }

    @Override
    public String getPresentableValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUserValue(T userValue) {
        this.userValue = userValue;
    }

    @Override
    public void updateUserValue(T userValue, boolean bulk) {
        this.userValue = userValue;
    }

    @Override
    public TextContentType getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(TextContentType contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DBObjectType getObjectType() {
        return objectType;
    }

    @Override
    public DBDataType getDataType() {
        return dataType;
    }

    @Override
    public Project getProject() {
        return project.ensure();
    }
}
