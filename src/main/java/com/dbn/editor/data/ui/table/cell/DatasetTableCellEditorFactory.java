package com.dbn.editor.data.ui.table.cell;

import com.dbn.common.dispose.Disposer;
import com.dbn.data.editor.ui.ListPopupValuesProvider;
import com.dbn.data.editor.ui.ListPopupValuesProviderBase;
import com.dbn.data.editor.ui.TextFieldWithPopup;
import com.dbn.data.model.ColumnInfo;
import com.dbn.data.type.DBDataType;
import com.dbn.data.type.GenericDataType;
import com.dbn.editor.data.model.DatasetEditorColumnInfo;
import com.dbn.editor.data.options.DataEditorSettings;
import com.dbn.editor.data.options.DataEditorValueListPopupSettings;
import com.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dbn.object.DBColumn;
import com.intellij.openapi.Disposable;

import javax.swing.table.TableCellEditor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetTableCellEditorFactory implements Disposable {
    private final Map<ColumnInfo, TableCellEditor> cache = new HashMap<>();

    public TableCellEditor getCellEditor(ColumnInfo columnInfo, DatasetEditorTable table) {
        TableCellEditor tableCellEditor = cache.get(columnInfo);
        if (tableCellEditor == null) {
            DBDataType dataType = columnInfo.getDataType();
            tableCellEditor =
                dataType.isNative() ? createEditorForNativeType(columnInfo, table) :
                dataType.getDeclaredType() != null ? createEditorForDeclaredType(columnInfo, table) : null;
            cache.put(columnInfo, tableCellEditor);
        }
        return tableCellEditor;
    }

    private static TableCellEditor createEditorForNativeType(ColumnInfo columnInfo, DatasetEditorTable table) {
        DataEditorSettings dataEditorSettings = DataEditorSettings.getInstance(table.getDatasetEditor().getProject());
        DBDataType dataType = columnInfo.getDataType();
        GenericDataType genericDataType = dataType.getGenericDataType();
        if (genericDataType == GenericDataType.NUMERIC) {
            return new DatasetTableCellEditor(table);
        }
        else if (genericDataType == GenericDataType.DATE_TIME) {
            DatasetTableCellEditorWithPopup tableCellEditor = new DatasetTableCellEditorWithPopup(table);
            tableCellEditor.getEditorComponent().createCalendarPopup(false);
            return tableCellEditor;
        }
        else if (genericDataType == GenericDataType.ARRAY) {
            DatasetTableCellEditorWithPopup tableCellEditor = new DatasetTableCellEditorWithPopup(table);
            tableCellEditor.getEditorComponent().createArrayEditorPopup(false);
            return tableCellEditor;
        }
        else if (genericDataType == GenericDataType.LITERAL) {
            long dataLength = dataType.getLength();


            if (dataLength < dataEditorSettings.getQualifiedEditorSettings().getTextLengthThreshold()) {
                DatasetTableCellEditorWithPopup tableCellEditor = new DatasetTableCellEditorWithPopup(table);

                DatasetEditorColumnInfo dseColumnInfo = (DatasetEditorColumnInfo) columnInfo;
                DBColumn column = dseColumnInfo.getColumn();
                TextFieldWithPopup editorComponent = tableCellEditor.getEditorComponent();
                DataEditorValueListPopupSettings valueListPopupSettings = dataEditorSettings.getValueListPopupSettings();

                if (!column.isPrimaryKey() && !column.isUniqueKey() && dataLength <= valueListPopupSettings.getDataLengthThreshold()) {
                    ListPopupValuesProvider valuesProvider = new ListPopupValuesProviderBase("Possible Values List", false) {
                        @Override
                        public List<String> getValues() {
                            return dseColumnInfo.getPossibleValues();
                        }
                    };
                    editorComponent.createValuesListPopup(valuesProvider, valueListPopupSettings.isShowPopupButton());
                }
                editorComponent.createTextEditorPopup(true);
                return tableCellEditor;
            } else {
                DatasetTableCellEditorWithTextEditor tableCellEditor = new DatasetTableCellEditorWithTextEditor(table);
                tableCellEditor.setEditable(false);
                return tableCellEditor;
            }

        } else if (genericDataType.isLOB()) {
            DatasetTableCellEditorWithTextEditor tableCellEditor = new DatasetTableCellEditorWithTextEditor(table);
            tableCellEditor.setEditable(false);
            return tableCellEditor;
        }
        return null;
    }

    private TableCellEditor createEditorForDeclaredType(ColumnInfo columnInfo, DatasetEditorTable table) {
        return null;
    }

    @Override
    public void dispose() {
        for (TableCellEditor cellEditor : cache.values()) {
            if (cellEditor instanceof Disposable) {
                Disposable disposable = (Disposable) cellEditor;
                Disposer.dispose(disposable);
            }
        }
        cache.clear();
    }
}
