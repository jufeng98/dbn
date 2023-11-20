package com.dbn.execution.method.history.ui;

import com.dbn.common.icon.Icons;
import com.dbn.common.ui.tree.DBNTreeNode;
import com.dbn.common.util.Commons;
import com.dbn.object.type.DBObjectType;
import lombok.Getter;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.List;

@Getter
public class MethodExecutionHistoryTreeNode extends DBNTreeNode {
    public enum Type {
        ROOT,
        CONNECTION,
        SCHEMA,
        PACKAGE,
        TYPE,
        PROCEDURE,
        FUNCTION,
        UNKNOWN
    }
    private final String name;
    private final Type type;

    public MethodExecutionHistoryTreeNode(MethodExecutionHistoryTreeNode parent, Type type, String name) {
        this.name = name;
        this.type = type;
        if (parent != null) {
            parent.add(this);
        }
    }
    public Icon getIcon() {
        return
            type == Type.CONNECTION? Icons.CONNECTION_CONNECTED :
            type == Type.SCHEMA ? Icons.DBO_SCHEMA :
            type == Type.PACKAGE ? Icons.DBO_PACKAGE :
            type == Type.TYPE ? Icons.DBO_TYPE :
            type == Type.PROCEDURE ? Icons.DBO_PROCEDURE :
            type == Type.FUNCTION ? Icons.DBO_FUNCTION : null;
    }

    public static Type getNodeType(DBObjectType objectType) {
        return
            objectType == DBObjectType.SCHEMA ? Type.SCHEMA :
            objectType == DBObjectType.PACKAGE ? Type.PACKAGE :
            objectType == DBObjectType.TYPE ? Type.TYPE :
            objectType == DBObjectType.PROCEDURE ||
                    objectType == DBObjectType.PACKAGE_PROCEDURE ||
                    objectType == DBObjectType.TYPE_PROCEDURE ? Type.PROCEDURE :
            objectType == DBObjectType.FUNCTION ||
                    objectType == DBObjectType.PACKAGE_FUNCTION ||
                    objectType == DBObjectType.TYPE_FUNCTION ? Type.FUNCTION : Type.UNKNOWN;
    }

    public List<TreeNode> getChildren() {
        return Commons.nvl(children, () -> Collections.emptyList());
    }

    @Override
    public boolean getAllowsChildren() {
        return
            type != Type.PROCEDURE &&
            type != Type.FUNCTION;
    }

    public boolean isValid() {
        return true;
    }
}
