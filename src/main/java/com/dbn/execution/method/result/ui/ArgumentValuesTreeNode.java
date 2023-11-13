package com.dbn.execution.method.result.ui;

import com.dbn.execution.method.ArgumentValue;
import com.dbn.object.common.DBObject;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class ArgumentValuesTreeNode implements TreeNode{
    private final Object userValue;
    private final ArgumentValuesTreeNode parent;
    private final List<ArgumentValuesTreeNode> children = new ArrayList<>();

    protected ArgumentValuesTreeNode(ArgumentValuesTreeNode parent, Object userValue) {
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
        this.userValue = userValue;
    }

    public Object getUserValue() {
        return userValue;
    }

    public List<ArgumentValuesTreeNode> getChildren() {
        return children;
    }

    public void dispose() {
        for (ArgumentValuesTreeNode treeNode : children) {
            treeNode.dispose();
        }
    }

    @Override
    public String toString() {
        if (userValue instanceof ArgumentValue) {
            ArgumentValue argumentValue = (ArgumentValue) userValue;
            return String.valueOf(argumentValue.getValue());
        }

        if (userValue instanceof DBObject) {
            DBObject object = (DBObject) userValue;
            return object.getName();
        }

        return userValue.toString();
    }

    /*********************************************************
     *                        TreeNode                       *
     *********************************************************/
    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(children);
    }
}
