package com.dbn.common.ui.table;

import javax.swing.*;
import java.awt.*;

public class Tables {
    private Tables() {}

    public static Rectangle getCellRectangle(JTable table, int row, int column) {
        Rectangle rectangle = table.getCellRect(row, column, true);

        rectangle.setLocation(
                (int) (rectangle.getX() + table.getLocationOnScreen().getX()),
                (int) (rectangle.getY() + table.getLocationOnScreen().getY()));
        return rectangle;
    }

    public static void selectCell(JTable table, int rowIndex, int columnIndex) {
        Rectangle cellRect = table.getCellRect(rowIndex, columnIndex, true);
        if (!table.getVisibleRect().contains(cellRect)) {
            table.scrollRectToVisible(cellRect);
        }
        if (table.getSelectedRowCount() != 1 || table.getSelectedRow() != rowIndex) {
            table.setRowSelectionInterval(rowIndex, rowIndex);
        }

        if (table.getSelectedColumnCount() != 1 || table.getSelectedColumn() != columnIndex) {
            table.setColumnSelectionInterval(columnIndex, columnIndex);
        }
    }
}
