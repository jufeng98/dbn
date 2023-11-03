package com.dci.intellij.dbn.execution.method.history.ui;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.type.DBObjectType;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.List;

import static com.dci.intellij.dbn.connection.ConnectionId.UNKNOWN;
import static com.dci.intellij.dbn.execution.method.history.ui.MethodExecutionHistoryTreeNode.Type.*;

public abstract class MethodExecutionHistoryTreeModel extends DefaultTreeModel implements StatefulDisposable {
    protected List<MethodExecutionInput> executionInputs;

    MethodExecutionHistoryTreeModel(List<MethodExecutionInput> executionInputs) {
        super(new DefaultMutableTreeNode());
        this.executionInputs = executionInputs;
        setRoot(new RootTreeNode());
    }

    @Override
    @Nullable
    public RootTreeNode getRoot() {
        return (RootTreeNode) super.getRoot();
    }

    public abstract List<MethodExecutionInput> getExecutionInputs();

    protected abstract String getMethodName(MethodExecutionInput executionInput);

    public abstract TreePath getTreePath(MethodExecutionInput executionInput);

    /**********************************************************
     *                        TreeNodes                       *
     **********************************************************/
    class RootTreeNode extends MethodExecutionHistoryTreeNode {
        RootTreeNode() {
            super(null, ROOT, "ROOT");
        }

        ConnectionTreeNode getConnectionNode(MethodExecutionInput executionInput) {
            if (!isLeaf())
                for (TreeNode node : getChildren()) {
                    ConnectionTreeNode connectionNode = (ConnectionTreeNode) node;
                    if (connectionNode.getConnectionId().equals(executionInput.getMethodRef().getConnectionId())) {
                        return connectionNode;
                    }
                }

            return new ConnectionTreeNode(this, executionInput);
        }
    }

    protected class ConnectionTreeNode extends MethodExecutionHistoryTreeNode {
        ConnectionRef connection;
        ConnectionTreeNode(MethodExecutionHistoryTreeNode parent, MethodExecutionInput executionInput) {
            super(parent, CONNECTION, null);
            this.connection = ConnectionRef.of(executionInput.getConnection());
        }

        ConnectionHandler getConnection() {
            return ConnectionRef.get(connection);
        }

        public ConnectionId getConnectionId() {
            ConnectionHandler connection = getConnection();
            return connection == null ? UNKNOWN : connection.getConnectionId();
        }

        @Override
        public String getName() {
            ConnectionHandler connection = getConnection();
            return connection == null ? "[unknown]" : connection.getName();
        }

        @Override
        public Icon getIcon() {
            ConnectionHandler connection = getConnection();
            return connection == null ? Icons.CONNECTION_INVALID : connection.getIcon();
        }

        SchemaTreeNode getSchemaNode(MethodExecutionInput executionInput) {
            if (!isLeaf())
                for (TreeNode node : getChildren()) {
                    SchemaTreeNode schemaNode = (SchemaTreeNode) node;
                    if (Strings.equalsIgnoreCase(schemaNode.getName(), executionInput.getMethodRef().getSchemaName())) {
                        return schemaNode;
                    }
                }
            return new SchemaTreeNode(this, executionInput);
        }
    }

    class SchemaTreeNode extends MethodExecutionHistoryTreeNode {
        SchemaTreeNode(MethodExecutionHistoryTreeNode parent, MethodExecutionInput executionInput) {
            super(parent, SCHEMA, executionInput.getMethodRef().getSchemaName());
        }

        ProgramTreeNode getProgramNode(MethodExecutionInput executionInput) {
            DBObjectRef<DBMethod> methodRef = executionInput.getMethodRef();
            DBObjectRef<?> programRef = methodRef.getParentRef(DBObjectType.PROGRAM);
            String programName = programRef.getObjectName();
            if (!isLeaf())
                for (TreeNode node : getChildren()) {
                    if (node instanceof ProgramTreeNode) {
                        ProgramTreeNode programNode = (ProgramTreeNode) node;
                        if (Strings.equalsIgnoreCase(programNode.getName(), programName)) {
                            return programNode;
                        }
                    }
                }
            return new ProgramTreeNode(this, executionInput);
        }

        MethodTreeNode getMethodNode(MethodExecutionInput executionInput) {
            if (!isLeaf())
                for (TreeNode node : getChildren()) {
                    if (node instanceof MethodTreeNode) {
                        MethodTreeNode methodNode = (MethodTreeNode) node;
                        if (methodNode.getExecutionInput() == executionInput) {
                            return methodNode;
                        }
                    }
                }
            return new MethodTreeNode(this, executionInput);
        }

    }

    class ProgramTreeNode extends MethodExecutionHistoryTreeNode {
        ProgramTreeNode(MethodExecutionHistoryTreeNode parent, MethodExecutionInput executionInput) {
            super(parent,
                    getNodeType(MethodRefUtil.getProgramObjectType(executionInput.getMethodRef())),
                    MethodRefUtil.getProgramName(executionInput.getMethodRef()));
        }

        MethodTreeNode getMethodNode(MethodExecutionInput executionInput) {
            DBObjectRef<DBMethod> methodRef = executionInput.getMethodRef();
            String methodName = methodRef.getObjectName();
            short overload = methodRef.getOverload();
            if (!isLeaf())
                for (TreeNode node : getChildren()) {
                    MethodTreeNode methodNode = (MethodTreeNode) node;
                    if (Strings.equalsIgnoreCase(methodNode.getName(), methodName) && methodNode.getOverload() == overload) {
                        return methodNode;
                    }
                }
            return new MethodTreeNode(this, executionInput);
        }
    }

    protected class MethodTreeNode extends MethodExecutionHistoryTreeNode {
        private final MethodExecutionInput executionInput;

        MethodTreeNode(MethodExecutionHistoryTreeNode parent, MethodExecutionInput executionInput) {
            super(parent,
                    getNodeType(executionInput.getMethodRef().getObjectType()),
                    getMethodName(executionInput));
            this.executionInput = executionInput;
        }

        short getOverload() {
            return executionInput.getMethodRef().getOverload();
        }

        MethodExecutionInput getExecutionInput() {
            return executionInput;
        }

        @Override
        public boolean isValid() {
            return !executionInput.isObsolete() && !executionInput.isInactive();
        }
    }


    /********************************************************
     *                    Disposable                        *
     ********************************************************/
    @Getter
    @Setter
    private boolean disposed;

    @Override
    public void disposeInner() {
        //executionInputs.clear();
    }
}
