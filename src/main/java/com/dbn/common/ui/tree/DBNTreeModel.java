package com.dbn.common.ui.tree;

import com.dbn.common.dispose.StatefulDisposable;
import lombok.Getter;
import lombok.Setter;

import javax.swing.tree.DefaultTreeModel;

@Getter
@Setter
public class DBNTreeModel extends DefaultTreeModel implements StatefulDisposable {
    private boolean disposed;

    public DBNTreeModel(DBNTreeNode root) {
        super(root);
    }


    public void disposeInner() {
        Object root = getRoot();
        if (root instanceof DBNTreeNode) {
            DBNTreeNode treeNode = (DBNTreeNode) root;
            treeNode.detach();
        }
    }
}
