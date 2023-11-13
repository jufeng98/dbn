package com.dbn.object.common.ui;

import com.dbn.object.DBSchema;
import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectTreeModel extends DefaultTreeModel {
    private TreePath initialSelection;
    private Object[] elements;

    public ObjectTreeModel(DBSchema schema, Set<DBObjectType> objectTypes, DBObject selectedObject) {
        super(new DefaultMutableTreeNode(schema == null ? "No schema selected" : schema.ref()));
        if (schema == null) return;


        DefaultMutableTreeNode rootNode = getRoot();

        for (DBObjectType objectType : objectTypes) {
            for (DBObject schemaObject :schema.collectChildObjects(objectType)) {
                DefaultMutableTreeNode objectNode = new DefaultMutableTreeNode(schemaObject.ref());
                rootNode.add(objectNode);
                if (selectedObject != null && selectedObject.equals(schemaObject)) {
                    initialSelection = new TreePath(objectNode.getPath());
                }
            }
        }

        for (DBObjectType schemaObjectType : schema.getObjectType().getChildren()) {
            if (hasChild(schemaObjectType, objectTypes)) {
                for (DBObject schemaObject : schema.collectChildObjects(schemaObjectType)) {
                    DefaultMutableTreeNode bundleNode = new DefaultMutableTreeNode(schemaObject.ref());

                    List<DBObject> objects = new ArrayList<>();
                    for (DBObjectType objectType : objectTypes) {
                        objects.addAll(schemaObject.collectChildObjects(objectType));
                    }

                    if (objects.size() > 0) {
                        rootNode.add(bundleNode);
                        for (DBObject object : objects) {
                            DefaultMutableTreeNode objectNode = new DefaultMutableTreeNode(object.ref());
                            bundleNode.add(objectNode);
                            if (selectedObject != null && selectedObject.equals(object)) {
                                initialSelection = new TreePath(objectNode.getPath());
                            }

                        }
                    }
                }
            }
        }
    }

    public TreePath getInitialSelection() {
        return initialSelection;
    }

    private boolean hasChild(DBObjectType parentObjectType, Set<DBObjectType> objectTypes) {
        for (DBObjectType objectType : objectTypes) {
            if (parentObjectType.hasChild(objectType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) super.getRoot();
    }

    public Object[] getAllElements() {
        if (elements == null) {
            List elementList = new ArrayList();
            collect(getRoot(), elementList);
            elements = elementList.toArray();
        }
        return elements;
    }

    private static void collect(TreeNode node, List<TreeNode> bucket) {
        bucket.add(node);
        for (int i=0; i<node.getChildCount(); i++) {
            TreeNode childNode = node.getChildAt(i);
            collect(childNode, bucket);
        }
    }
}
