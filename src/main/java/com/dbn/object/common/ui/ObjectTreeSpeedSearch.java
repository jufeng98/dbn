package com.dbn.object.common.ui;

import com.dbn.object.lookup.DBObjectRef;
import com.intellij.ui.SpeedSearchBase;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class ObjectTreeSpeedSearch extends SpeedSearchBase {

    public ObjectTreeSpeedSearch(ObjectTree objectTree) {
        super(objectTree);
    }

    @Override
    public ObjectTree getComponent() {
        return (ObjectTree) super.getComponent();
    }

    @Override
    protected int getSelectedIndex() {
        TreePath selectionPath = getComponent().getSelectionPath();
        if (selectionPath != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
            for (int i=0; i<getAllElements().length; i++) {
                if (getAllElements()[i] == node) {
                    return i;
                }
            }
        }
        return 0;
    }

    @NotNull
    @Override
    protected Object[] getAllElements() {
        return getComponent().getModel().getAllElements();
    }

    @Override
    protected String getElementText(Object obj) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
        Object userObject = node.getUserObject();
        if (userObject instanceof DBObjectRef) {
            DBObjectRef objectRef = (DBObjectRef) userObject;
            return objectRef.getObjectName();
        }
        return userObject.toString();
    }

    @Override
    protected void selectElement(Object obj, String s) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
        TreeUtil.selectPath(getComponent(), new TreePath(node.getPath()), true);
    }
}
