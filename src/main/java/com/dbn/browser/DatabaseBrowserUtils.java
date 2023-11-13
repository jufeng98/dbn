package com.dbn.browser;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.common.action.UserDataKeys;
import com.dbn.common.collections.CompactArrayList;
import com.dbn.common.filter.Filter;
import com.dbn.object.common.DBObjectBundle;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

import static com.dbn.common.dispose.Checks.isTrue;
import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class DatabaseBrowserUtils {
    @Nullable
    public static TreePath createTreePath(@NotNull BrowserTreeNode treeNode) {
        try {
            Project project = treeNode.getProject();
            DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
            boolean isTabbedMode = browserManager.isTabbedMode();

            int treeDepth = treeNode.getTreeDepth();
            int nodeIndex = isTabbedMode ? treeDepth - 1 : treeDepth + 1;
            if (nodeIndex < 0) {
                return null;
            }

            BrowserTreeNode[] path = new BrowserTreeNode[nodeIndex];
            while (treeNode != null) {
                treeDepth = treeNode.getTreeDepth();
                path[isTabbedMode ? treeDepth -2 : treeDepth] = treeNode;
                if (treeNode instanceof DatabaseBrowserManager) break;
                if (isTabbedMode && treeNode instanceof DBObjectBundle) break;
                treeNode = treeNode.getParent();
            }
            return new TreePath(path);
        } catch (IllegalArgumentException | IllegalStateException | ProcessCanceledException e) {
            conditionallyLog(e);
            // workaround for TreePath "Path elements must be non-null"
            return null;
        }
    }

    public static boolean treeVisibilityChanged(
            List<BrowserTreeNode> possibleTreeNodes,
            List<BrowserTreeNode> actualTreeNodes,
            Filter<BrowserTreeNode> filter) {
        for (BrowserTreeNode treeNode : possibleTreeNodes) {
            if (treeNode != null) {
                if (filter.accepts(treeNode)) {
                    if (!actualTreeNodes.contains(treeNode)) return true;
                } else {
                    if (actualTreeNodes.contains(treeNode)) return true;
                }
            }
        }
        return false;
    }

    public static List<BrowserTreeNode> createList(BrowserTreeNode... treeNodes) {
        List<BrowserTreeNode> treeNodeList = new ArrayList<>();
        for (BrowserTreeNode treeNode : treeNodes) {
            if (treeNode != null) {
                treeNodeList.add(treeNode);
            }
        }
        return new CompactArrayList<>(treeNodeList);
    }

    public static void markSkipBrowserAutoscroll(VirtualFile file) {
        file.putUserData(UserDataKeys.SKIP_BROWSER_AUTOSCROLL, true);
    }

    public static void unmarkSkipBrowserAutoscroll(VirtualFile file) {
        file.putUserData(UserDataKeys.SKIP_BROWSER_AUTOSCROLL, false);
    }

    static boolean isSkipBrowserAutoscroll(VirtualFile file) {
        return isTrue(file.getUserData(UserDataKeys.SKIP_BROWSER_AUTOSCROLL));
    }
}
