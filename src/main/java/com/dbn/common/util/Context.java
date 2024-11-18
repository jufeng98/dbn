package com.dbn.common.util;

import com.dbn.common.ui.form.DBNForm;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static com.dbn.common.util.Unsafe.cast;

public interface Context {
    static DataContext getDataContext(DBNForm form) {
        return getDataContext(form.getComponent());
    }

    static DataContext getDataContext(Editor editor) {
        return getDataContext(editor.getComponent());
    }

    static DataContext getDataContext(FileEditor editor) {
        return getDataContext(editor.getComponent());
    }

    static DataContext getDataContext(Component component) {
        return DataManager.getInstance().getDataContext(component);
    }

    @Nullable
    static <T> T getData(Component component, String dataId) {
        DataContext dataContext = getDataContext(component);
        return cast(dataContext.getData(DataKey.create(dataId)));
    }

    @Nullable
    static <T> T getData(FileEditor component, String dataId) {
        DataContext dataContext = getDataContext(component);
        return cast(dataContext.getData(DataKey.create(dataId)));
    }

}
