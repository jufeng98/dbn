package com.dci.intellij.dbn.data.editor.ui.array;

import com.dci.intellij.dbn.common.ui.list.EditableStringList;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.TableCellEditor;
import java.awt.*;

public class ArrayEditorList extends EditableStringList {
    public ArrayEditorList(ArrayEditorPopupProviderForm parent) {
        super(parent, false, true);
    }

    @Override
    public @NotNull ArrayEditorPopupProviderForm getParentComponent() {
        return (ArrayEditorPopupProviderForm) super.getParentComponent();
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int rowIndex, int columnIndex) {
        Component component = super.prepareEditor(editor, rowIndex, columnIndex);
        component.addKeyListener(getParentComponent());
        return component;
    }
}
