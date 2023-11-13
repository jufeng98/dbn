package com.dci.intellij.dbn.editor.data.structure;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Safe;
import com.dci.intellij.dbn.language.psql.structure.PSQLStructureViewElement;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Comparator;

public class DatasetEditorStructureViewModelSorter implements Sorter {

    @Override
    public Comparator getComparator() {
        return COMPARATOR;    
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    @NotNull
    public ActionPresentation getPresentation() {
        return ACTION_PRESENTATION;
    }

    @Override
    @NotNull
    public String getName() {
        return "Sort by Name";
    }

    private static final ActionPresentation ACTION_PRESENTATION = new ActionPresentation() {
        @Override
        @NotNull
        public String getText() {
            return "Sort by Name";
        }

        @Override
        public String getDescription() {
            return "Sort elements alphabetically by name";
        }

        @Override
        public Icon getIcon() {
            return Icons.ACTION_SORT_ALPHA;
        }
    };

    private static final Comparator COMPARATOR = new Comparator() {
        @Override
        public int compare(Object object1, Object object2) {
            if (object1 instanceof DatasetEditorStructureViewElement && object2 instanceof DatasetEditorStructureViewElement) {
                DatasetEditorStructureViewElement structureViewElement1 = (DatasetEditorStructureViewElement) object1;
                DatasetEditorStructureViewElement structureViewElement2 = (DatasetEditorStructureViewElement) object2;
                BrowserTreeNode treeNode1 = structureViewElement1.getValue();
                BrowserTreeNode treeNode2 = structureViewElement2.getValue();
                return Safe.compare(
                        treeNode1.getName(),
                        treeNode2.getName());
            } else {
                return object1 instanceof PSQLStructureViewElement ? 1 : -1;
            }
        }
    };
}
