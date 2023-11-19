package com.dbn.execution.method.result.ui;

import com.dbn.common.color.Colors;
import com.dbn.common.icon.Icons;
import com.dbn.common.ui.tree.DBNColoredTreeCellRenderer;
import com.dbn.common.ui.tree.DBNTree;
import com.dbn.common.util.TextAttributes;
import com.dbn.data.grid.color.DataGridTextAttributesKeys;
import com.dbn.data.type.DBDataType;
import com.dbn.execution.method.ArgumentValue;
import com.dbn.object.DBArgument;
import com.dbn.object.DBMethod;
import com.dbn.object.DBTypeAttribute;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.object.type.DBObjectType;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.sql.ResultSet;
import java.util.List;

import static com.dbn.common.util.Strings.cachedLowerCase;

class ArgumentValuesTree extends DBNTree{

    ArgumentValuesTree(MethodExecutionResultForm parent, List<ArgumentValue> inputArgumentValues, List<ArgumentValue> outputArgumentValues) {
        super(parent, createModel(parent, inputArgumentValues, outputArgumentValues));
        setCellRenderer(new CellRenderer());
        Color bgColor = TextAttributes.getSimpleTextAttributes(DataGridTextAttributesKeys.PLAIN_DATA).getBgColor();
        setBackground(bgColor == null ? Colors.getTableBackground() : bgColor);

        addTreeSelectionListener(createTreeSelectionListener());
    }


    @NotNull
    public MethodExecutionResultForm getParentForm() {
        return this.getParentComponent();
    }

    @NotNull
    private static ArgumentValuesTreeModel createModel(MethodExecutionResultForm parentForm, List<ArgumentValue> inputArgumentValues, List<ArgumentValue> outputArgumentValues) {
        return new ArgumentValuesTreeModel(parentForm.getMethod(), inputArgumentValues, outputArgumentValues);
    }

    private TreeSelectionListener createTreeSelectionListener() {
        return e -> {
            TreePath path = e.getPath();
            ArgumentValuesTreeNode treeNode = (ArgumentValuesTreeNode) path.getLastPathComponent();
            if (treeNode == null) return;

            Object userValue = treeNode.getUserValue();
            if (userValue instanceof ArgumentValue) {
                ArgumentValue argumentValue = (ArgumentValue) userValue;
                DBArgument argument = argumentValue.getArgument();
                if (argument == null || !argument.isOutput()) return;

                Object value = argumentValue.getValue();
                if (value instanceof ResultSet || argumentValue.isLargeObject() || argumentValue.isLargeValue()) {
                    getParentForm().selectArgumentOutputTab(argument);
                }
            }
        };
    }

    static class CellRenderer extends DBNColoredTreeCellRenderer {
        @Override
        public void customizeCellRenderer(@NotNull DBNTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            ArgumentValuesTreeNode treeNode = (ArgumentValuesTreeNode) value;
            Object userValue = treeNode.getUserValue();
            if (userValue instanceof DBMethod) {
                DBMethod method = (DBMethod) userValue;
                setIcon(method.getIcon());
                append(method.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }

            if (userValue instanceof String) {
                append((String) userValue, treeNode.isLeaf() ?
                        SimpleTextAttributes.REGULAR_ATTRIBUTES :
                        SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
            }

            if (userValue instanceof DBObjectRef) {
                DBObjectRef<DBArgument> argumentRef = (DBObjectRef<DBArgument>) userValue;
                DBArgument argument = DBObjectRef.get(argumentRef);
                setIcon(argument == null ? Icons.DBO_ARGUMENT : argument.getIcon());
                append(argumentRef.getObjectName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }

            if (userValue instanceof ArgumentValue) {
                ArgumentValue argumentValue = (ArgumentValue) userValue;
                DBArgument argument = argumentValue.getArgument();
                DBTypeAttribute attribute = argumentValue.getAttribute();
                Object originalValue = argumentValue.getValue();
                String displayValue = originalValue instanceof ResultSet || argumentValue.isLargeObject() || argumentValue.isLargeValue() ? "" : String.valueOf(originalValue);

                if (attribute == null) {
                    if (argument == null) {
                        setIcon(DBObjectType.ARGUMENT.getIcon());
                        append("[unknown]", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        append(" = ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    } else{
                        setIcon(argument.getIcon());
                        append(argument.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        append(" = ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        DBDataType dataType = argument.getDataType();
                        if (dataType != null) {
                            append("{" + cachedLowerCase(dataType.getName()) + "} " , SimpleTextAttributes.GRAY_ATTRIBUTES);
                        }
                    }
                    append(displayValue, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                } else {
                    setIcon(attribute.getIcon());
                    append(attribute.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    append(" = ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    DBDataType dataType = attribute.getDataType();
                    if (dataType != null) {
                        append("{" + dataType.getName() + "} " , SimpleTextAttributes.GRAY_ATTRIBUTES);
                    }
                    append(displayValue, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                }
            }
        }
    }
}
