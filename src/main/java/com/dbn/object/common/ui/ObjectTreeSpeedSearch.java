package com.dbn.object.common.ui;

import com.dbn.common.compatibility.Compatibility;
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
        if (selectionPath == null) return -1;

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
        for (int i=0; i<getAllElements().length; i++) {
            if (getAllElements()[i] == node) {
                return i;
            }
        }
        return 0;
    }

    private Object[] getElements() {
        return getComponent().getModel().getAllElements();
    }

    @NotNull
    @Override
    @Compatibility
    protected Object[] getAllElements() {
        return getElements();
    }

    @Override
    protected int getElementCount() {
        return getElements().length;
    }

    //@Override
    protected Object getElementAt(int viewIndex) {
        return getElements()[viewIndex];
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
