package com.dbn.object.common.ui;

import com.dbn.common.ui.component.DBNComponent;
import com.dbn.common.ui.tree.DBNTree;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.TreeModel;

public class ObjectTree extends DBNTree {

    public ObjectTree(@NotNull DBNComponent parent) {
        super(parent, new ObjectTreeModel(null, null, null));
        setCellRenderer(new ObjectTreeCellRenderer());
        new ObjectTreeSpeedSearch(this);
    }

    @Override
    public void setModel(TreeModel newModel) {
        super.setModel(newModel);
    }

    @Override
    public ObjectTreeModel getModel() {
        return (ObjectTreeModel) super.getModel();
    }

}
