package com.dbn.editor.data.structure;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.editor.data.DatasetEditor;
import com.dbn.editor.data.model.DatasetEditorModel;
import com.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import com.dbn.object.DBSchema;
import com.dbn.object.common.DBObjectBundle;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class DatasetEditorStructureViewElement implements StructureViewTreeElement, Comparable{
    private BrowserTreeNode treeNode;
    private DatasetEditor datasetEditor;
    private StructureViewTreeElement[] children;

    DatasetEditorStructureViewElement(BrowserTreeNode treeNode, DatasetEditor datasetEditor) {
        this.treeNode = treeNode;
        this.datasetEditor = datasetEditor;
    }

    @Override
    public BrowserTreeNode getValue() {
        return treeNode;
    }

    @Override
    @NotNull
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            public String getPresentableText() {
                return treeNode.getPresentableText();
            }

            @Override
            @Nullable
            public String getLocationString() {
                if (treeNode instanceof DBColumn) {
                    DBColumn column  = (DBColumn) treeNode;
                    return column.getDataType().getName().toLowerCase();

                }
                return null;
            }

            @Override
            @Nullable
            public Icon getIcon(boolean open) {
                return treeNode.getIcon(0);
            }

            @Nullable
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }
        };
    }

    @Override
    @NotNull
    public StructureViewTreeElement[] getChildren() {
        if (children == null) {
            if (datasetEditor.isDisposed())  {
                return EMPTY_ARRAY;
            }
            DBDataset dataset = datasetEditor.getDataset();
            if (treeNode instanceof DBObjectBundle) {
                DatasetEditorStructureViewElement schemaStructureElement =
                        new DatasetEditorStructureViewElement(dataset.getSchema(), datasetEditor);
                children = new StructureViewTreeElement[] {schemaStructureElement};
            }
            else if (treeNode instanceof DBSchema) {
                DatasetEditorStructureViewElement datasetStructureElement =
                        new DatasetEditorStructureViewElement(dataset, datasetEditor);
                children = new StructureViewTreeElement[] {datasetStructureElement};
            }
            else if (treeNode instanceof DBDataset) {
                List<DBColumn> columns = dataset.getColumns();
                children = new StructureViewTreeElement[columns.size()];
                for (int i=0; i<children.length; i++) {
                    children[i] = new DatasetEditorStructureViewElement(columns.get(i), datasetEditor);
                }
            }
            else {
                children = EMPTY_ARRAY;
            }
        }
        return children;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (!datasetEditor.isDisposed()) {
            DatasetEditorTable table = datasetEditor.getEditorTable();
            table.cancelEditing();
            DatasetEditorModel model = table.getModel();
            if (treeNode instanceof DBColumn &&  model.getRowCount() > 0) {
                DBColumn column = (DBColumn) treeNode;
                int modelColumnIndex = model.getHeader().indexOfColumn(column);
                int tableColumnIndex = table.convertColumnIndexToView(modelColumnIndex);
                int rowIndex = table.getSelectedRow();
                if (rowIndex == -1)  rowIndex = 0;
                if (tableColumnIndex == -1) tableColumnIndex = 0;
                table.selectCell(rowIndex, tableColumnIndex);
                if (requestFocus) {
                    table.requestFocus();
                }
            }
        }
    }

    @Override
    public boolean canNavigate() {
        return !datasetEditor.isDisposed();
    }

    @Override
    public boolean canNavigateToSource() {
        return treeNode instanceof DBColumn && !datasetEditor.isDisposed() && datasetEditor.getEditorTable().getRowCount() > 0;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        DatasetEditorStructureViewElement desve = (DatasetEditorStructureViewElement) o;
        if (treeNode instanceof DBColumn && desve.treeNode instanceof DBColumn) {
            DBColumn thisColumn = (DBColumn) treeNode;
            DBColumn remoteColumn = (DBColumn) desve.treeNode;
            return thisColumn.compareTo(remoteColumn);
        }
        return 0;
    }
}
