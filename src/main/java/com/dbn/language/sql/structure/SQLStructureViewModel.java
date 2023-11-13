package com.dbn.language.sql.structure;

import com.dbn.language.common.structure.DBLanguageStructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class SQLStructureViewModel extends DBLanguageStructureViewModel {
    private final Filter[] filters = new Filter[]{new SQLStructureViewModelFilter()};

    SQLStructureViewModel(Editor editor, PsiFile psiFile) {
        super(editor, psiFile);
    }

    @Override
    @NotNull
    public StructureViewTreeElement getRoot() {
        return new SQLStructureViewElement(getPsiFile());
    }

    @NotNull
    @Override
    public Filter[] getFilters() {
        return filters;
    }
}
