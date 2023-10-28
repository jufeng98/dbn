package com.dci.intellij.dbn.common.ui.list;

import com.dci.intellij.dbn.common.ui.component.DBNComponent;
import com.dci.intellij.dbn.common.ui.table.DBNEditableTable;
import com.dci.intellij.dbn.common.ui.table.DBNTableGutter;
import com.dci.intellij.dbn.common.ui.table.IndexTableGutter;
import com.dci.intellij.dbn.common.ui.util.Borders;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class EditableStringList extends DBNEditableTable<EditableStringListModel> {
    private final boolean sorted;
    private final boolean indexed;

    public EditableStringList(@NotNull DBNComponent parent, boolean sorted, boolean indexed) {
        this(parent, new ArrayList<>(), sorted, indexed);
    }

    public EditableStringList(@NotNull DBNComponent parent, List<String> elements, boolean sorted, boolean indexed) {
        super(parent, new EditableStringListModel(elements, sorted), false);
        setTableHeader(null);
        this.sorted = sorted;
        this.indexed = indexed;

        if (indexed) {
            getColumnModel().getColumn(0).setPreferredWidth(20);
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    accommodateColumnsSize();
                }
            });
        }

        addKeyListener(keyListener);
    }

    @Override
    public DBNTableGutter<?> createTableGutter() {
        return indexed ? new IndexTableGutter<>(this) : null;
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int rowIndex, int columnIndex) {
        JTextField component = (JTextField) super.prepareEditor(editor, rowIndex, columnIndex);
        component.setBorder(Borders.EMPTY_BORDER);
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() != EditableStringList.this) {
                    editor.stopCellEditing();
                }
            }
        });

        component.addKeyListener(keyListener);
        return component;
    }

    private final KeyAdapter keyListener = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.isConsumed()) return;

            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_BACK_SPACE) {
                String value = getValue(e);
                if (value != null && value.isEmpty()) {
                    e.consume();
                    removeRow();
                } else {
                    updateValue(e);
                }
            } else {
                updateValue(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isConsumed()) return;

            int selectedRow = getSelectedRow();
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_DOWN) {
                if (selectedRow == getModel().getRowCount() - 1) {
                    e.consume();
                    insertRow();
                }
            } else if (keyCode == KeyEvent.VK_ENTER && e.getModifiers() == 0) {
                e.consume();
                insertRow();
            } else if (keyCode == KeyEvent.VK_DELETE) {
                String value = getValue(e);
                if (value != null && value.isEmpty()) {
                    e.consume();
                    removeRow();
                } else {
                    updateValue(e);
                }
            }
        }

        private String getValue(KeyEvent e) {
            Object source = e.getSource();
            return source instanceof EditableStringList ?
                    (String) getModel().getValueAt(getSelectedRow(), 0) :
                    ((JTextField) source).getText();
        }

        private void updateValue(KeyEvent e) {
            if (e.getSource() instanceof JTextField) {
                String value = getValue(e);
                getModel().setValueAt(value, getSelectedRow(), 0);
            }
        }
    };

    @Override
    public Component getEditorComponent() {
        return super.getEditorComponent();
    }

    public List<String> getStringValues() {
        return getModel().getData();
    }

    public void setStringValues(List<String> stringValues) {
        setModel(new EditableStringListModel(stringValues, sorted));
    }


}
