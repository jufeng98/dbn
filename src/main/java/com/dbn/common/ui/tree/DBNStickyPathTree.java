package com.dbn.common.ui.tree;

import com.dbn.browser.options.DatabaseBrowserSettings;
import com.dbn.common.ui.util.Borders;
import com.dbn.common.ui.util.UserInterface;
import com.intellij.ide.ui.laf.darcula.DarculaUIUtil;
import com.intellij.ui.components.JBLayeredPane;
import com.intellij.util.Alarm;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.dbn.common.dispose.Failsafe.nn;
import static com.dbn.common.thread.Dispatch.alarm;
import static com.dbn.common.thread.Dispatch.alarmRequest;
import static com.dbn.common.ui.util.UserInterface.getParentOfType;

public class DBNStickyPathTree extends DBNTree{
    private static final TreeModel EMPTY_TREE_MODEL = new EmptyTreeModel();
    public static final int VERTICAL_SHIFT = 6;
    private final JScrollPane scrollPane;
    private final JPanel headerPanel;
    private final Container container;

    private TreePath currentTreePath;
    private final Alarm refreshAlarm = alarm(this);

    @Getter
    @Setter
    private boolean featureEnabled;


    public DBNStickyPathTree(@NotNull DBNTree sourceTree) {
        super(sourceTree);
        setBackground(sourceTree.getBackground());
        setBorder(sourceTree.getBorder());
        setRootVisible(sourceTree.isRootVisible());
        setShowsRootHandles(sourceTree.getShowsRootHandles());
        setRowHeight(sourceTree.getRowHeight());
        setCellRenderer(sourceTree.getCellRenderer());
        setPreferredSize(new Dimension(-1, 0));
        //setBackground(Colors.lafDarker(sourceTree.getBackground(), 1));

        scrollPane = nn(getParentOfType(sourceTree, JScrollPane.class));
        container = scrollPane.getParent();

        JBLayeredPane layeredPane = new JBLayeredPane();
        UserInterface.replaceComponent(scrollPane, layeredPane);
        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);


        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(-1, 0));
        headerPanel.add(this, BorderLayout.CENTER);
        headerPanel.setBorder(Borders.lineBorder(DarculaUIUtil.getOutlineColor(false, false), 0, 0, 1, 0));
        headerPanel.setBackground(sourceTree.getBackground());
        layeredPane.add(headerPanel, JLayeredPane.PALETTE_LAYER);

        container.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeScrollPane();
                resizeHeaderOverlay();
            }
        });

        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(e -> {
            //if (e.getValueIsAdjusting()) return;
            refreshHeaderOverlay();
        });
        
        sourceTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                refreshHeaderOverlay();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                refreshHeaderOverlay();
            }
        });

        sourceTree.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                boolean featureEnabled = checkFeatureEnabled();
                if (isFeatureEnabled() != featureEnabled) {
                    setFeatureEnabled(featureEnabled);
                    refreshHeaderOverlay();
                }
            }
        });

        addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath treePath = event.getPath();
                DBNTree sourceTree = getSourceTree();
                sourceTree.getSelectionModel().setSelectionPath(treePath);
                sourceTree.collapsePath(treePath);

                Rectangle bounds = sourceTree.getPathBounds(treePath);
                if (bounds == null) return;

                int x = (int) bounds.getX();
                int y = (int) (bounds.getY() - getOverlayHigh());
                bounds.setLocation(x, y);
                sourceTree.scrollRectToVisible(bounds);
                refreshHeaderOverlay();
            }
        });
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        // TODO propagate clicks to source tree
        if (true) {
            super.processMouseEvent(e);
            return;
        }
        TreePath treePath = Trees.getPathForLocation(this, e.getPoint());

        DBNTree sourceTree = getSourceTree();
        Rectangle pathBounds = sourceTree.getPathBounds(treePath);
        if (pathBounds == null) return;

        sourceTree.dispatchEvent(new MouseEvent(
                sourceTree,
                e.getID(),
                e.getWhen(),
                e.getModifiersEx(),
                (int) pathBounds.getX(),
                (int) pathBounds.getY(),
                e.getXOnScreen(),
                e.getYOnScreen(),
                e.getClickCount(),
                e.isPopupTrigger(),
                e.getButton()));
    }

    protected boolean checkFeatureEnabled() {
        return true;
    }

    private void resizeHeaderOverlay() {
        int width = container.getWidth() - 10/*- scrollPane.getVerticalScrollBar().getWidth()*/;
        int height = getOverlayHigh();
        headerPanel.setBounds(0, 0, width, height);
    }

    private void resizeScrollPane() {
        int width = container.getWidth();
        int height = container.getHeight();
        scrollPane.setBounds(0, 0, width, height);
    }

    private int getOverlayHigh() {
        return (int) headerPanel.getPreferredSize().getHeight();
    }

    private DBNTree getSourceTree() {
        return getParentComponent();
    }


    private void refreshHeaderOverlay() {
        alarmRequest(refreshAlarm, 0, true, () -> renderHeaderOverlay());
    }


    private void renderHeaderOverlay() {
        TreePath parentPath = resolveHiddenTreePath();
        if (Objects.equals(currentTreePath, parentPath)) return;
        currentTreePath = parentPath;

        int visibleRows = parentPath == null ? 0 : rootVisible ? parentPath.getPathCount() : parentPath.getPathCount() - 1;
        setVisibleRowCount(visibleRows);

        int height = visibleRows > 0 ? visibleRows * getRowHeight() + 8 : 0;
        headerPanel.setPreferredSize(new Dimension(-1, height));
        resizeHeaderOverlay();
        resizeScrollPane();

        if (visibleRows > 0) {
            setVisible(true);
            setModel(new PathTreeModel(parentPath));
            Trees.expandAll(this);
        } else {
            setVisible(false);
            setModel(EMPTY_TREE_MODEL);
        }

        UserInterface.repaint(scrollPane);
    }

    @Nullable
    private TreePath resolveHiddenTreePath() {
        if (!featureEnabled) return null;

        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();

        int verticalScroll = scrollBar.getValue();
        if (verticalScroll < getRowHeight()) return null;

        verticalScroll = verticalScroll + getOverlayHigh();
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
