package com.dbn.code.common.completion.options.filter.ui;

import com.dbn.code.common.completion.options.filter.CodeCompletionFilterSettings;

import javax.swing.tree.DefaultTreeModel;

public class CodeCompletionFilterTreeModel extends DefaultTreeModel {
    private CodeCompletionFilterTreeNode root;

    public CodeCompletionFilterTreeModel(CodeCompletionFilterSettings setup) {
        super(setup.createCheckedTreeNode());
        root = (CodeCompletionFilterTreeNode) getRoot();
    }

    public void applyChanges() {
        root.applyChanges();
    }

    public void resetChanges() {
        root.resetChanges();        
    }
/*
    public Object getChild(Object o, int i) {
        CheckedTreeNode node = (CheckedTreeNode) o;
        return node.getChildAt(i);
    }

    public int getChildCount(Object o) {
        CheckedTreeNode node = (CheckedTreeNode) o;
        return node.getChildCount();
    }

    public boolean isLeaf(Object o) {
        CheckedTreeNode node = (CheckedTreeNode) o;
        return node.isLeaf();
    }


    public int getIndexOfChild(Object o, Object o1) {
        CheckedTreeNode node = (CheckedTreeNode) o;
        return node.getIndex((TreeNode) o1);
    }*/
}
