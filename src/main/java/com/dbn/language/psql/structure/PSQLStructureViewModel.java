package com.dbn.language.psql.structure;

import com.dbn.language.common.structure.DBLanguageStructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.NodeProvider;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class PSQLStructureViewModel extends DBLanguageStructureViewModel {
    private static final Collection<NodeProvider> NODE_PROVIDERS = Collections.singletonList(new ShowDetailsNodeProvider());


    private final Sorter[] sorters = new Sorter[] {new PSQLStructureViewModelSorter()};
    private final Grouper[] groupers = new Grouper[]{new PSQLStructureViewModelGrouper()};
    private final Filter[] filters = new Filter[]{new PSQLStructureViewModelFilter()};

    PSQLStructureViewModel(Editor editor, PsiFile psiFile) {
        super(editor, psiFile);
    }

    @Override
    @NotNull
    public StructureViewTreeElement getRoot() {
        return new PSQLStructureViewElement(getPsiFile());
    }

    @Override
    @NotNull
    public Grouper[] getGroupers() {
        return groupers;
    }

    @Override
    @NotNull
    public Sorter[] getSorters() {
        return sorters;
    }

    @NotNull
    @Override
    public Filter[] getFilters() {
        return filters;
    }

    @NotNull
    @Override
    public Collection getNodeProviders() {
        return Collections.emptyList();//NODE_PROVIDERS;
    }
}