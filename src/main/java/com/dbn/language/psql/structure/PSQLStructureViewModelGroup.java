package com.dbn.language.psql.structure;

import com.dbn.common.util.Naming;
import com.dbn.object.type.DBObjectType;
import com.intellij.ide.util.treeView.smartTree.Group;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PSQLStructureViewModelGroup implements Group {
    private static final TextAttributesKey TEXT_ATTRIBUTES_KEY =
            TextAttributesKey.createTextAttributesKey(
                    "PSQLStructureViewModelGroup",
                    new TextAttributes(JBColor.BLACK, null, null, null, Font.BOLD));

    private final DBObjectType objectType;
    private final List<TreeElement> children = new ArrayList<>();


    PSQLStructureViewModelGroup(DBObjectType objectType) {
        this.objectType = objectType;
    }

    public void addChild(TreeElement treeElement) {
        children.add(treeElement);
    }

    @Override
    @NotNull
    public ItemPresentation getPresentation() {
        return itemPresentation;
    }

    @Override
    @NotNull
    public Collection<TreeElement> getChildren() {
        return children;
    }


    private final ItemPresentation itemPresentation = new ItemPresentation(){
        @Override
        public String getPresentableText() {
            return Naming.capitalize(objectType.getListName());
        }

        @Override
        public String getLocationString() {
            return null;
        }

        @Override
        public Icon getIcon(boolean open) {
            return null;//objectType.getListIcon();
        }

        public TextAttributesKey getTextAttributesKey() {
            return TEXT_ATTRIBUTES_KEY;
        }
    };
}