package com.dbn.editor.session.ui.table;

import com.dbn.common.ref.WeakRef;
import com.dbn.editor.session.model.SessionBrowserColumnInfo;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SessionBrowserTableHeaderMouseListener extends MouseAdapter {
    private final WeakRef<SessionBrowserTable> table;

    public SessionBrowserTableHeaderMouseListener(SessionBrowserTable table) {
        this.table = WeakRef.of(table);
    }

    @NotNull
    public SessionBrowserTable getTable() {
        return table.ensure();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            SessionBrowserTable table = getTable();
            Point mousePoint = e.getPoint();
            int tableColumnIndex = table.getTableHeader().columnAtPoint(mousePoint);
            if (tableColumnIndex > -1) {
                int modelColumnIndex = table.convertColumnIndexToModel(tableColumnIndex);
                if (modelColumnIndex > -1) {
                    SessionBrowserColumnInfo columnInfo = (SessionBrowserColumnInfo) table.getModel().getColumnInfo(modelColumnIndex);
                    table.showPopupMenu(e, null, columnInfo);
                }
            }
        }
    }
}
