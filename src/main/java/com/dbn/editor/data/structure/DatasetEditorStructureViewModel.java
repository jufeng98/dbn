package com.dbn.editor.data.structure;

import com.dbn.common.editor.structure.DBObjectStructureViewModel;
import com.dbn.common.ref.WeakRef;
import com.dbn.editor.data.DatasetEditor;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatasetEditorStructureViewModel extends DBObjectStructureViewModel {
    private final Sorter[] sorters = new Sorter[] {new DatasetEditorStructureViewModelSorter()};
    private final WeakRef<DatasetEditor> datasetEditor;
    private StructureViewTreeElement root;

    public DatasetEditorStructureViewModel(DatasetEditor datasetEditor) {
        this.datasetEditor = WeakRef.of(datasetEditor);
    }

    @NotNull
    public DatasetEditor getDatasetEditor() {
        return WeakRef.ensure(datasetEditor);
    }

    @NotNull
    @Override
    public Sorter[] getSorters() {
        return sorters;
    }

    @Override
    @Nullable
    public Object getCurrentEditorElement() {
        return null;
    }

    @Override
    @NotNull
    public StructureViewTreeElement getRoot() {
        if (root == null) {
            //DBObjectBundle objectBundle = datasetEditor.getCache().getObjectBundle();
            DatasetEditor datasetEditor = getDatasetEditor();
            root = new DatasetEditorStructureViewElement(datasetEditor.getDataset(), datasetEditor);
        }
        return root;
    }
}
