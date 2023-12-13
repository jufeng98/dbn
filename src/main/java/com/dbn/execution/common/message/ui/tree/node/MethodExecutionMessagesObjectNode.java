package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.common.dispose.Failsafe;
import com.dbn.common.ui.tree.TreeEventType;
import com.dbn.common.ui.tree.Trees;
import com.dbn.execution.common.message.ui.tree.MessagesTreeBundleNode;
import com.dbn.execution.method.MethodExecutionMessage;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;

public class MethodExecutionMessagesObjectNode extends MessagesTreeBundleNode<MethodExecutionMessagesNode, MethodExecutionMessageNode> {
    private final DBEditableObjectVirtualFile file;

    MethodExecutionMessagesObjectNode(@NotNull MethodExecutionMessagesNode parent, @NotNull DBEditableObjectVirtualFile file) {
        super(parent);
        this.file = file;
    }

    @NotNull
    @Override
    public DBEditableObjectVirtualFile getFile() {
        return Failsafe.nn(file);
    }

    public DBSchemaObject getObject() {
        return file.getObject();
    }

    TreePath addCompilerMessage(MethodExecutionMessage executionMessage) {
        clearChildren();
        MethodExecutionMessageNode messageNode = new MethodExecutionMessageNode(this, executionMessage);
        addChild(messageNode);

        TreePath treePath = Trees.createTreePath(this);
        getTreeModel().notifyTreeModelListeners(treePath, TreeEventType.STRUCTURE_CHANGED);
        return treePath;
    }

    @Nullable
    public TreePath getTreePath(MethodExecutionMessage executionMessage) {
        for (MethodExecutionMessageNode messageNode : getChildren()) {
            if (messageNode.getMessage() == executionMessage) {
                return Trees.createTreePath(messageNode);
            }
        }
        return null;
    }
}
