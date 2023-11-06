package com.dci.intellij.dbn.data.grid.ui.table.basic;

import com.dci.intellij.dbn.common.ui.table.DBNTableGutter;
import com.dci.intellij.dbn.common.ui.util.UserInterface;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.util.Objects;

public class BasicTableGutter<T extends BasicTable> extends DBNTableGutter<T> {
    public BasicTableGutter(@NotNull T table) {
        super(table);
        addListSelectionListener(gutterSelectionListener);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (getModel().getSize() == 0) {
            setFixedCellWidth(10);
        }
        table.getSelectionModel().addListSelectionListener(tableSelectionListener);
        table.addPropertyChangeListener(e -> {
            Object newProperty = e.getNewValue();
            if (newProperty instanceof Font) {
                Font font = (Font) newProperty;
                setFont(font);
                setFixedCellHeight(table.getRowHeight());
                ListCellRenderer cellRenderer = getCellRenderer();
                if (cellRenderer instanceof Component) {
                    Component component = (Component) cellRenderer;
                    component.setFont(font);
                }
            } else if (Objects.equals(e.getPropertyName(), "rowHeight")) {
                setFixedCellHeight(table.getRowHeight());
            }
        });
    }

    @Override
    protected ListCellRenderer createCellRenderer() {
        return new BasicTableGutterCellRenderer();
    }

    @Override
    public void scrollRectToVisible(Rectangle rect) {
        super.scrollRectToVisible(rect);

        T table = getTable();
        Rectangle tableRect = table.getVisibleRect();

        tableRect.y = rect.y;
        tableRect.height = rect.height;
        table.scrollRectToVisible(tableRect);
    }

    boolean justGainedFocus = false;

    @Override
    protected void processFocusEvent(FocusEvent e) {
        if (isDisposed()) return;
        super.processFocusEvent(e);
        if (e.getComponent() == this) {
            justGainedFocus = e.getID() == FocusEvent.FOCUS_GAINED;
        }
    }

    /*********************************************************
     *                ListSelectionListener                  *
     *********************************************************/
    private ListSelectionListener gutterSelectionListener = e -> {
        T table = getTable();
        if (hasFocus()) {
            if (justGainedFocus) {
                justGainedFocus = false;
                if (table.isEditing()) {
                    TableCellEditor cellEditor = table.getCellEditor();
                    if (cellEditor != null) {
                        cellEditor.cancelCellEditing();
                    }
                }
                table.clearSelection();
                int columnCount = table.getColumnCount();
                if (columnCount > 0) {
                    int lastColumnIndex = Math.max(columnCount - 1, 0);
                    table.setColumnSelectionInterval(0, lastColumnIndex);
                }
            }

            for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
                ListSelectionModel selectionModel = table.getSelectionModel();
                if (isSelectedIndex(i))
                    selectionModel.addSelectionInterval(i, i); else
                    selectionModel.removeSelectionInterval(i, i);
            }
        }
    };

    private ListSelectionListener tableSelectionListener = e -> {
        UserInterface.repaint(BasicTableGutter.this);
    };

    @Override
    public void disposeInner() {
        getTable().getSelectionModel().removeListSelectionListener(tableSelectionListener);
        removeListSelectionListener(gutterSelectionListener);
        tableSelectionListener = null;
        gutterSelectionListener = null;
        super.disposeInner();
    }
}
