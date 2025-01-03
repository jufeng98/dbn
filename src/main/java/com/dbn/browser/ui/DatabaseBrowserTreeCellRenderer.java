package com.dbn.browser.ui;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.browser.model.LoadInProgressTreeNode;
import com.dbn.browser.options.DatabaseBrowserSettings;
import com.dbn.common.ui.tree.DBNColoredTreeCellRenderer;
import com.dbn.common.ui.tree.DBNTree;
import com.dbn.common.ui.tree.Trees;
import com.dbn.common.util.Commons;
import com.dbn.common.util.Strings;
import com.dbn.connection.ConnectionBundle;
import com.dbn.connection.ConnectionHandler;
import com.dbn.object.DBColumn;
import com.dbn.object.DBSchema;
import com.dbn.object.DBUser;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.common.list.DBObjectList;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class DatabaseBrowserTreeCellRenderer implements TreeCellRenderer {
    private final DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
    private final DatabaseBrowserSettings browserSettings;

    public DatabaseBrowserTreeCellRenderer(@NotNull Project project) {
        browserSettings = DatabaseBrowserSettings.getInstance(project);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        return cellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    }

    private class DefaultTreeCellRenderer extends DBNColoredTreeCellRenderer {
        @Override
        public Font getFont() {
            Font font = super.getFont();
            return font == null ? UIUtil.getTreeFont() : font;
        }

        @Override
        public void customizeCellRenderer(DBNTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof LoadInProgressTreeNode loadInProgressTreeNode) {
                setIcon(loadInProgressTreeNode.getIcon(0));
                append("Loading...", SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES);
                return;
            }

            if (!(value instanceof BrowserTreeNode treeNode)) return;

            setIcon(treeNode.getIcon(0));

            boolean isDirty = false;
            String displayName;
            if (treeNode instanceof ConnectionBundle) {
                displayName = "PROJECT";
            } else {
                displayName = treeNode.getPresentableText();
            }

            if (treeNode instanceof DBObjectList<?> objectsList) {
                boolean isEmpty = objectsList.getChildCount() == 0;
                isDirty = /*objectsList.isDirty() ||*/ objectsList.isLoading() || (!objectsList.isLoaded() && !hasConnectivity(objectsList));
                SimpleTextAttributes textAttributes =
                        isDirty ? SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES :
                                isEmpty ? SimpleTextAttributes.REGULAR_ATTRIBUTES :
                                        SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES;

                append(Commons.nvl(displayName, ""), textAttributes);

                // todo display load error
                    /*
                                SimpleTextAttributes descriptionTA = objectsList.getErrorMessage() == null ?
                                        SimpleTextAttributes.GRAY_ATTRIBUTES : SimpleTextAttributes.ERROR_ATTRIBUTES;
                                append(" " + displayDetails, descriptionTA);

                                if (objectsList.getErrorMessage() != null) {
                                    String msg = "Could not load " + displayName + ". Cause: " + objectsList.getErrorMessage();
                                    setToolTipText(msg);
                                }  else {
                                    setToolTipText(null);
                                }
                    */
            } else {
                boolean showBold = false;
                boolean showGrey = false;
                boolean isDisposed = false;
                if (treeNode instanceof DBObject object) {
                    if (object instanceof DBSchema schema) {
                        showBold = schema.isUserSchema();
                        showGrey = schema.isEmptySchema();
                    } else if (object instanceof DBUser user) {
                        showBold = user.isSessionUser();
                        showGrey = user.isExpired();
                    } else if (object instanceof DBSchemaObject schemaObject) {
                        showGrey = schemaObject.isDisabled();
                    }

                    isDisposed = object.isDisposed();
                }

                if (!showGrey && treeNode instanceof DBColumn column) {
                    showGrey = column.isAudit();
                }

                SimpleTextAttributes textAttributes =
                        isDisposed ? SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES :
                                showBold ? (showGrey ? SimpleTextAttributes.GRAYED_BOLD_ATTRIBUTES : SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES) :
                                        (showGrey ? SimpleTextAttributes.GRAYED_ATTRIBUTES : SimpleTextAttributes.REGULAR_ATTRIBUTES);

                if (displayName == null) displayName = "displayName null!!";

                append(displayName, textAttributes);

                Trees.applySpeedSearchHighlighting(tree, this, true, selected);
            }
            String displayDetails = treeNode.getPresentableTextDetails();
            if (!Strings.isEmptyOrSpaces(displayDetails)) {
                append(" " + displayDetails, isDirty ? SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES : SimpleTextAttributes.GRAY_ATTRIBUTES);
            }


            if (browserSettings.getGeneralSettings().isShowObjectDetails()) {
                String conditionalDetails = treeNode.getPresentableTextConditionalDetails();
                if (!Strings.isEmptyOrSpaces(conditionalDetails)) {
                    append(" - " + conditionalDetails, SimpleTextAttributes.GRAY_ATTRIBUTES);
                }

            }
        }

        private boolean hasConnectivity(@NotNull DBObjectList<?> objectsList) {
            ConnectionHandler connection = objectsList.getConnection();
            return connection.canConnect() && connection.isValid();
        }
    }
}
