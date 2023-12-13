package com.dbn.execution.common.message.ui.tree;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.message.MessageType;
import com.dbn.connection.ConnectionId;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.List;

public interface MessagesTreeNode<P extends MessagesTreeNode, C extends MessagesTreeNode> extends TreeNode, StatefulDisposable {
    P getParent();

    default MessagesTreeModel getTreeModel() {
        return getParent().getTreeModel();
    }

    @Nullable
    default VirtualFile getFile() {return null;}

    default List<C> getChildren() {return Collections.emptyList();}

    default boolean hasMessageChildren(MessageType type) {return false;}

    default void removeMessages(@NotNull ConnectionId connectionId) {};

    @Nullable
    default ConnectionId getConnectionId() {return null;};
}
