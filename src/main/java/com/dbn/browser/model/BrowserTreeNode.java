package com.dbn.browser.model;

import com.dbn.browser.ui.ToolTipProvider;
import com.dbn.connection.DatabaseEntity;
import com.dbn.object.type.DBObjectType;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public interface BrowserTreeNode extends TreeNode, NavigationItem, ItemPresentation, ToolTipProvider, DatabaseEntity {
    void initTreeElement();

    boolean canExpand();

    int getTreeDepth();

    boolean isTreeStructureLoaded();

    List<? extends BrowserTreeNode> getChildren();

    void refreshTreeChildren(@NotNull DBObjectType... objectTypes);

    void rebuildTreeChildren();

    Icon getIcon(int flags);

    String getPresentableTextDetails();

    String getPresentableTextConditionalDetails();

    @Override
    BrowserTreeNode getChildAt(int index);

    @Override
    @Nullable
    BrowserTreeNode getParent();

    int getIndex(BrowserTreeNode child);

    default String getLocationString() {
        return null;
    }

    @Override
    default Enumeration<? extends BrowserTreeNode> children() {
        return Collections.enumeration(getChildren());
    }

    @Override
    default int getIndex(TreeNode child) {
        return getIndex((BrowserTreeNode) child);
    }

    @Override
    default boolean getAllowsChildren() {
        return !isLeaf();
    }
}
