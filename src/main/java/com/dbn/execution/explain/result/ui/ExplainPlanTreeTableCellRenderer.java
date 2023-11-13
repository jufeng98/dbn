package com.dbn.execution.explain.result.ui;

import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.ui.treeStructure.treetable.TreeTableCellRenderer;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;

import javax.swing.*;
import java.awt.*;

public class ExplainPlanTreeTableCellRenderer extends TreeTableCellRenderer {
    private final TreeTableTree tree;

    public ExplainPlanTreeTableCellRenderer(TreeTable treeTable, TreeTableTree tree) {
        super(treeTable, tree);
        this.tree = tree;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int modelRow  = table.convertRowIndexToModel(row);
        //TableModel model = myTreeTable.getModel();
        //myTree.setTreeTableTreeBorder(hasFocus && model.getColumnClass(column).equals(TreeTableModel.class) ? myDefaultBorder : null);
        tree.setVisibleRow(modelRow);

        final Object treeObject = tree.getPathForRow(modelRow).getLastPathComponent();
        boolean leaf = tree.getModel().isLeaf(treeObject);
        final boolean expanded = tree.isExpanded(modelRow);
        Component component = tree.getCellRenderer().getTreeCellRendererComponent(tree, treeObject, isSelected, expanded, leaf, modelRow, hasFocus);
        if (component instanceof JComponent) {
            table.setToolTipText(((JComponent)component).getToolTipText());
        }

        //myTree.setCellFocused(false);

        return tree;
    }


}
