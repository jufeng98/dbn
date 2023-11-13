package com.dbn.object.action;

import com.dbn.common.action.ContextAction;
import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class AnObjectAction<T extends DBObject> extends ContextAction<T> {
    private final DBObjectRef<T> object;
    private boolean custom;

    public AnObjectAction(String text, Icon icon, @NotNull T object) {
        super(text, null, icon);
        this.object = DBObjectRef.of(object);
        custom = true;
    }
    public AnObjectAction(@NotNull T object) {
        super(object.getName(), null, object.getIcon());
        this.object = DBObjectRef.of(object);
    }

    @Override
    protected T getTarget(@NotNull AnActionEvent e) {
        return getTarget();
    }

    public T getTarget() {
        return DBObjectRef.get(object);
    }

    @NotNull
    @Override
    public  Project getProject() {
        return object.ensure().getProject();
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable T target) {
        if (custom) return;
        if (target == null) return;
        presentation.setText(target.getName(), false);
    }
}
