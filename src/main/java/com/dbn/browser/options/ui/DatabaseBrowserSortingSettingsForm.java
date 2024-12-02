package com.dbn.browser.options.ui;

import com.dbn.browser.options.DatabaseBrowserSortingSettings;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.ui.form.DBNForm;
import com.dbn.common.ui.table.DBNColoredTableCellRenderer;
import com.dbn.common.ui.table.DBNEditableTable;
import com.dbn.common.ui.table.DBNEditableTableModel;
import com.dbn.common.ui.table.DBNTable;
import com.dbn.common.ui.util.Borders;
import com.dbn.common.ui.util.Cursors;
import com.dbn.object.common.sorting.DBObjectComparator;
import com.dbn.object.common.sorting.DBObjectComparators;
import com.dbn.object.common.sorting.SortingType;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.ComboBoxTableRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.dbn.common.util.Strings.cachedUpperCase;

public class DatabaseBrowserSortingSettingsForm extends ConfigurationEditorForm<DatabaseBrowserSortingSettings> {
    private JPanel mainPanel;
    private JBScrollPane sortingTypesScrollPanel;
    private final JTable sortingTypeTable;

    public DatabaseBrowserSortingSettingsForm(DatabaseBrowserSortingSettings settings) {
        super(settings);
        sortingTypeTable = new SortingTypeTable(this, settings.getComparators());
        sortingTypesScrollPanel.setViewportView(sortingTypeTable);
        registerComponent(sortingTypeTable);
    }


    @Override
    public void applyFormChanges() throws ConfigurationException {
        SortingTypeTableModel model = (SortingTypeTableModel) sortingTypeTable.getModel();
        getConfiguration().setComparators(model.comparators);
    }

    @Override
    public void resetFormChanges() {
        sortingTypeTable.setModel(new SortingTypeTableModel(getConfiguration().getComparators()));
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public static class SortingTypeTable extends DBNEditableTable<SortingTypeTableModel> {

        public SortingTypeTable(DBNForm parent, Collection<DBObjectComparator<?>> comparators) {
            super(parent, new SortingTypeTableModel(comparators), true);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            adjustRowHeight(3);

            setDefaultRenderer(DBObjectType.class, new DBNColoredTableCellRenderer() {
                @Override
                protected void customizeCellRenderer(DBNTable<?> table, Object value, boolean selected, boolean hasFocus, int row, int column) {
                    DBObjectType objectType = (DBObjectType) value;
                    if (objectType != null) {
                        setIcon(objectType.getIcon());
                        append(cachedUpperCase(objectType.getName()), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    } else {
                        append("");
                    }
                    setBorder(SELECTION_BORDER);
                }
            });

            setDefaultRenderer(SortingType.class, new DBNColoredTableCellRenderer() {
                @Override
                protected void customizeCellRenderer(DBNTable<?> table, Object value, boolean selected, boolean hasFocus, int row, int column) {
                    SortingType sortingType = (SortingType) value;
                    append(sortingType.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    setBorder(SELECTION_BORDER);
                }
            });

            ComboBoxTableRenderer<SortingType> editor = new ComboBoxTableRenderer<>(SortingType.values()) {
            };
            editor.setBorder(Borders.TEXT_FIELD_INSETS);
            setDefaultEditor(SortingType.class, editor);

            getSelectionModel().addListSelectionListener(e -> {
                //noinspection StatementWithEmptyBody
                if (!e.getValueIsAdjusting()) {
                    //editCellAt(getSelectedRows()[0], getSelectedColumns()[0]);
                }
            });
        }

        @Override
        protected void processMouseMotionEvent(MouseEvent e) {
            Point mouseLocation = e.getPoint();
            int columnIndex = columnAtPoint(mouseLocation);
            if (columnIndex == 1) {
                setCursor(Cursors.handCursor());
            } else {
                setCursor(Cursors.defaultCursor());
            }
            super.processMouseMotionEvent(e);
        }
    }

    public static class SortingTypeTableModel extends DBNEditableTableModel {
        private final List<DBObjectComparator<?>> comparators;

        public SortingTypeTableModel(Collection<DBObjectComparator<?>> comparators) {
            this.comparators = new ArrayList<>(comparators);
        }

        @Override
        public int getRowCount() {
            return comparators.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> "Object Type";
                case 1 -> "Sorting Type";
                default -> null;
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> DBObjectType.class;
                case 1 -> SortingType.class;
                default -> null;
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> false;
                case 1 -> true;
                default -> false;
            };
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            DBObjectComparator<?> comparator = comparators.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> comparator.getObjectType();
                case 1 -> comparator.getSortingType();
                default -> null;
            };
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                SortingType sortingType = (SortingType) value;
                if (sortingType != null) {
                    DBObjectComparator<?> comparator = comparators.remove(rowIndex);
                    comparators.add(rowIndex, DBObjectComparators.predefined(comparator.getObjectType(), sortingType));
                }
            }
        }

        @Override
        public void insertRow(int rowIndex) {
            throw new UnsupportedOperationException("Row mutation not supported");
        }

        @Override
        public void removeRow(int rowIndex) {
            throw new UnsupportedOperationException("Row mutation not supported");
        }
    }
}
