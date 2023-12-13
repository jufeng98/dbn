package com.dbn.execution.method.history.ui;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.icon.Icons;
import com.dbn.common.ui.tree.DBNTreeModel;
import com.dbn.common.util.Strings;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.ConnectionRef;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.object.DBMethod;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Collections;
import java.util.List;

import static com.dbn.connection.ConnectionId.UNKNOWN;

public abstract class MethodExecutionHistoryTreeModel extends DBNTreeModel implements StatefulDisposable {
    protected List<MethodExecutionInput> executionInputs;

    MethodExecutionHistoryTreeModel(List<MethodExecutionInput> executionInputs) {
        super(new RootTreeNode());
        this.executionInputs = executionInputs;
    }

    @Override
    @Nullable
    public RootTreeNode getRoot() {
        return (RootTreeNode) super.getRoot();
    }

    public abstract List<MethodExecutionInput> getExecutionInputs();

    public abstract TreePath getTreePath(MethodExecutionInput executionInput);

    /**********************************************************
     *                        TreeNodes                       *
     **********************************************************/
    static class RootTreeNode extends MethodExecutionHistoryTreeNode {
        RootTreeNode() {
            super(null, Type.ROOT, "ROOT");
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

    protected static class ConnectionTreeNode extends MethodExecutionHistoryTreeNode {
        ConnectionRef connection;
        ConnectionTreeNode(MethodExecutionHistoryTreeNode parent, MethodExecutionInput executionInput) {
            super(parent, Type.CONNECTION, null);
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

    static class SchemaTreeNode extends MethodExecutionHistoryTreeNode {
        SchemaTreeNode(MethodExecutionHistoryTreeNode parent, MethodExecutionInput executionInput) {
            super(parent, Type.SCHEMA, executionInput.getMethodRef().getSchemaName());
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

    static class ProgramTreeNode extends MethodExecutionHistoryTreeNode {
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

    protected static class MethodTreeNode extends MethodExecutionHistoryTreeNode {
        private final MethodExecutionInput executionInput;

        MethodTreeNode(MethodExecutionHistoryTreeNode parent, MethodExecutionInput executionInput) {
            super(parent,
                    getNodeType(executionInput.getMethodRef().getObjectType()),
                    parent instanceof ProgramTreeNode ?
                            executionInput.getMethodRef().getObjectName() :
                            executionInput.getMethodRef().getQualifiedObjectName());
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

    @Override
    public void disposeInner() {
        super.disposeInner();
        setRoot(new RootTreeNode());
        executionInputs = Collections.emptyList();
    }
}
