package com.dbn.language.psql.structure;

import com.intellij.ide.util.FileStructureNodeProvider;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.openapi.actionSystem.Shortcut;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class ShowDetailsNodeProvider implements FileStructureNodeProvider {
    public static final String ID = "SHOW_DETAILS";

    @NotNull
    @Override
    public Collection<TreeElement> provideNodes(@NotNull TreeElement node) {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public ActionPresentation getPresentation() {
        return new ActionPresentationData("Show details", null, null);
    }

    @NotNull
    @Override
    public String getName() {
        return ID;
    }

    @NotNull
    @Override
    public String getCheckBoxText() {
        return "Show details";
    }

    @NotNull
    @Override
    public Shortcut[] getShortcut() {
        return new Shortcut[0];
    }
}
