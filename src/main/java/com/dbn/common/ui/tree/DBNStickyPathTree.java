package com.dbn.common.ui.tree;

import com.dbn.common.color.Colors;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.util.Borders;
import com.dbn.common.ui.util.UserInterface;
import com.intellij.ide.ui.laf.darcula.DarculaUIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.dbn.common.dispose.Failsafe.nn;
import static com.dbn.common.ui.util.UserInterface.getParentOfType;

public class DBNStickyPathTree extends DBNTree{
    private static final TreeModel EMPTY_TREE_MODEL = new EmptyTreeModel();
    private final JScrollPane scrollPane;
    private final JPanel headerPanel;
    private TreePath currentTreePath;


    public DBNStickyPathTree(@NotNull DBNTree sourceTree) {
        super(sourceTree);
        setRootVisible(sourceTree.isRootVisible());
        setShowsRootHandles(sourceTree.getShowsRootHandles());
        setCellRenderer(sourceTree.getCellRenderer());
        setPreferredSize(new Dimension(-1, 0));
        //setBackground(Colors.lafDarker(sourceTree.getBackground(), 1));

        scrollPane = nn(getParentOfType(sourceTree, JScrollPane.class));
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(this, BorderLayout.CENTER);
        headerPanel.setBorder(Borders.lineBorder(DarculaUIUtil.getOutlineColor(false, false), 0, 0, 1, 0));
        scrollPane.setColumnHeaderView(headerPanel);

        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(e -> {
            if (e.getValueIsAdjusting()) return;
            renderHiddenPath();
        });


        addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {

            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath treePath = event.getPath();
                collapseHiddenPath(treePath);
            }
        });
    }

    private DBNTree getSourceTree() {
        return getParentComponent();
    }


    private void collapseHiddenPath(TreePath treePath) {
        Dispatch.run(() -> {
            DBNTree sourceTree = getSourceTree();
            sourceTree.getSelectionModel().setSelectionPath(treePath);
            sourceTree.collapsePath(treePath);
            sourceTree.scrollPathToVisible(treePath);
            renderHiddenPath();
        });
    }

    private void renderHiddenPath() {
        TreePath parentPath = resolveHiddenTreePath();
        if (Objects.equals(currentTreePath, parentPath)) return;
        currentTreePath = parentPath;

        int visibleRows = parentPath == null ? 0 : rootVisible ? parentPath.getPathCount() : parentPath.getPathCount() - 1;
        setVisibleRowCount(visibleRows);

        int height = visibleRows > 0 ? visibleRows * getRowHeight() + 8 : 0;
        headerPanel.setPreferredSize(new Dimension(-1, height));

        if (visibleRows > 0) {
            setVisible(true);
            setModel(new PathTreeModel(parentPath));
            DBNTree sourceTree = getSourceTree();
            setSelectionPath(sourceTree.getSelectionPath());
            Trees.expandAll(this);

        } else {
            setVisible(false);
            setModel(EMPTY_TREE_MODEL);
        }

        UserInterface.repaint(scrollPane);
    }

    @Nullable
    private TreePath resolveHiddenTreePath() {
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        int verticalScroll = scrollBar.getValue();

        DBNTree sourceTree = getSourceTree();
        TreePath treePath = sourceTree.getClosestPathForLocation(0, verticalScroll);
        return treePath.getParentPath();
    }


    private static class PathTreeModel implements ReadonlyTreeModel {
        private final List<?> path;

        public PathTreeModel(TreePath path) {
            this.path = Arrays.asList(path.getPath());
        }

        @Override
        public Object getRoot() {
            return path.isEmpty() ? null : path.get(0);
        }

        @Override
        public Object getChild(Object parent, int index) {
            int parentIndex = path.indexOf(parent);
            return index == 0 ? path.get(parentIndex + 1) : null;
        }

        @Override
        public int getChildCount(Object parent) {
            int parentIndex = path.indexOf(parent);
            return parentIndex < path.size() - 1 ? 1 : 0;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            return 0;
        }

        @Override
        public boolean isLeaf(Object node) {
            return false;
        }
    }
}
