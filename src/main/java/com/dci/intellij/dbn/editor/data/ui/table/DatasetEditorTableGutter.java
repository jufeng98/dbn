package com.dci.intellij.dbn.editor.data.ui.table;

import com.dci.intellij.dbn.common.ui.util.Mouse;
import com.dci.intellij.dbn.data.grid.ui.table.basic.BasicTableGutter;
import com.dci.intellij.dbn.editor.data.ui.table.renderer.DatasetEditorTableGutterRenderer;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DatasetEditorTableGutter extends BasicTableGutter<DatasetEditorTable> {
    public DatasetEditorTableGutter(DatasetEditorTable table) {
        super(table);
        addMouseListener(mouseListener);
    }

    @Override
    protected ListCellRenderer<?> createCellRenderer() {
        return new DatasetEditorTableGutterRenderer();
    }

    MouseListener mouseListener = Mouse.listener().onClick(e -> {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            getTable().getDatasetEditor().openRecordEditor(getSelectedIndex());
        }
    });

    @Override
    public void disposeInner() {
        removeMouseListener(mouseListener);
        mouseListener = null;
        super.disposeInner();
    }
}
