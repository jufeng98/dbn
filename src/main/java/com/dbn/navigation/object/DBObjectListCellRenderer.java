package com.dbn.navigation.object;

import com.dbn.common.dispose.Failsafe;
import com.dbn.connection.ConnectionHandler;
import com.dbn.object.common.DBObject;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.JList;

public class DBObjectListCellRenderer extends ColoredListCellRenderer {
    public static final DBObjectListCellRenderer INSTANCE = new DBObjectListCellRenderer();

    private DBObjectListCellRenderer() {}

    @Override
    protected void customizeCellRenderer(@NotNull JList list, Object value, int index, boolean selected, boolean hasFocus) {
        if (value instanceof DBObject) {
            DBObject object = (DBObject) value;
            setIcon(object.getIcon());
            append(object.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            ConnectionHandler connection = Failsafe.nn(object.getConnection());
            append(" [" + connection.getName() + "]", SimpleTextAttributes.GRAY_ATTRIBUTES);
            if (object.getParentObject() != null) {
                append(" - " + object.getParentObject().getQualifiedName(), SimpleTextAttributes.GRAY_ATTRIBUTES);
            }
        } else {
            append(value.toString(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
    }
}
