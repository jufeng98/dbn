package com.dbn.common.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class DBNTreeNode extends DefaultMutableTreeNode {
    public DBNTreeNode() {
    }

    public DBNTreeNode(Object userObject) {
        super(userObject);
    }

    public void detach() {
        if (children == null) return;
        if (children.isEmpty()) return;

        for (TreeNode child : children) {
            if (child instanceof DBNTreeNode) {
                DBNTreeNode treeNode = (DBNTreeNode) child;
                treeNode.detach();
            }
        }

        children.clear();
    }
}
