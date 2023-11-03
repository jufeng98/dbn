package com.dci.intellij.dbn.editor.session.ui.table;

import com.dci.intellij.dbn.common.ui.util.Mouse;
import com.dci.intellij.dbn.data.grid.ui.table.basic.BasicTableGutter;
import com.dci.intellij.dbn.data.grid.ui.table.basic.BasicTableGutterCellRenderer;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SessionBrowserTableGutter extends BasicTableGutter<SessionBrowserTable> {
    public SessionBrowserTableGutter(SessionBrowserTable table) {
        super(table);
        addMouseListener(mouseListener);
    }

    @Override
    protected ListCellRenderer<?> createCellRenderer() {
        return new BasicTableGutterCellRenderer();
    }

    MouseListener mouseListener = Mouse.listener().onClick(e -> {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            // TODO
        }
    });

    @Override
    public void disposeInner() {
        removeMouseListener(mouseListener);
        mouseListener = null;
        super.disposeInner();
    }
}
