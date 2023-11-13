package com.dbn.object.dependency.ui;

import com.dbn.common.util.Commons;
import com.dbn.object.common.DBObject;
import com.intellij.openapi.Disposable;
import com.intellij.ui.SpeedSearchBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class ObjectDependencyTreeSpeedSearch extends SpeedSearchBase<JTree> implements Disposable {
    private static final Object[] EMPTY_ARRAY = new Object[0];

    public ObjectDependencyTreeSpeedSearch(ObjectDependencyTree tree) {
        super(tree);
    }

    @Override
    protected int getSelectedIndex() {
        Object[] elements = getAllElements();
        ObjectDependencyTreeNode treeNode = getSelectedTreeElement();
        if (treeNode != null) {
            for (int i=0; i<elements.length; i++) {
                if (treeNode == elements[i]) {
                    return i;
                }
            }
        }
        return -1;
    }

    private ObjectDependencyTreeNode getSelectedTreeElement() {
        TreePath selectionPath = getComponent().getSelectionPath();
        if (selectionPath != null) {
            return (ObjectDependencyTreeNode) selectionPath.getLastPathComponent();
        }
        return null;
    }

    @NotNull
    @Override
    protected Object[] getAllElements() {
        List<ObjectDependencyTreeNode> nodes = new ArrayList<>();
        ObjectDependencyTreeNode root = getComponent().getModel().getRoot();
        loadElements(nodes, root);
        return nodes.toArray();
    }

    private static void loadElements(List<ObjectDependencyTreeNode> nodes, ObjectDependencyTreeNode browserTreeNode) {
        nodes.add(browserTreeNode);
        List<ObjectDependencyTreeNode> children = browserTreeNode.getChildren(false);
        if (children != null) {
            for (ObjectDependencyTreeNode child : children) {
                loadElements(nodes, child);
            }

        }
    }

    @Override
    public ObjectDependencyTree getComponent() {
        return (ObjectDependencyTree) super.getComponent();
    }

    @Override
    @Nullable
    protected String getElementText(Object o) {
        ObjectDependencyTreeNode treeNode = (ObjectDependencyTreeNode) o;
        DBObject object = treeNode.getObject();

        ObjectDependencyTreeNode rootNode = treeNode.getModel().getRoot();
        DBObject rootObject = rootNode.getObject();
        if (rootObject != null && object != null) {
            if (Commons.match(rootObject.getSchema(), object.getSchema())) {
                return object.getName();
            } else {
                return object.getSchema().getName() + "." + object.getName();
            }
        }

        return null;
    }

    @Override
    protected void selectElement(Object o, String s) {
        ObjectDependencyTreeNode treeNode = (ObjectDependencyTreeNode) o;
        getComponent().selectElement(treeNode);
    }


    @Override
    public void dispose() {
    }
}
