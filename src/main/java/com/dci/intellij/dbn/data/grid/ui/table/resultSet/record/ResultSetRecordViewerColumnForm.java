package com.dci.intellij.dbn.data.grid.ui.table.resultSet.record;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.data.model.ColumnInfo;
import com.dci.intellij.dbn.data.model.resultSet.ResultSetDataModelCell;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

import static com.dci.intellij.dbn.data.grid.options.DataGridSettings.isAuditColumn;

public class ResultSetRecordViewerColumnForm extends DBNFormBase {
    private JLabel columnLabel;
    private JPanel valueFieldPanel;
    private JLabel dataTypeLabel;
    private JPanel mainPanel;

    private final JTextField valueTextField;
    private ResultSetDataModelCell<?, ?> cell;

    public ResultSetRecordViewerColumnForm(ResultSetRecordViewerForm parent, ResultSetDataModelCell<?, ?> cell, boolean showDataType) {
        super(parent);
        ColumnInfo columnInfo = cell.getColumnInfo();

        DBDataType dataType = columnInfo.getDataType();
        boolean auditColumn = isAuditColumn(getProject(), columnInfo.getName());

        columnLabel.setIcon(Icons.DBO_COLUMN);
        columnLabel.setText(columnInfo.getName());
        columnLabel.setForeground(auditColumn ? UIUtil.getLabelDisabledForeground() : UIUtil.getLabelForeground());
        if (showDataType) {
            dataTypeLabel.setText(dataType.getQualifiedName());
            dataTypeLabel.setForeground(UIUtil.getInactiveTextColor());
        } else {
            dataTypeLabel.setVisible(false);
        }

        valueTextField = new JTextField();
        valueTextField.setPreferredSize(new Dimension(200, -1));
        valueTextField.addKeyListener(keyAdapter);

        valueFieldPanel.add(valueTextField, BorderLayout.CENTER);
        valueTextField.setEditable(false);
        valueTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        valueTextField.setBackground(Colors.getTextFieldBackground());
        setCell(cell);
    }

    public String getColumnName() {
        return columnLabel.getText();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public void setCell(ResultSetDataModelCell<?, ?> cell) {
        this.cell = cell;

        if (cell.getUserValue() instanceof String) {
            String userValue = (String) cell.getUserValue();
            if (userValue.indexOf('\n') > -1) {
                userValue = userValue.replace('\n', ' ');
            } else {
            }
            valueTextField.setText(userValue);
        } else {
            String presentableValue = cell.getFormatter().formatObject(cell.getUserValue());
            valueTextField.setText(presentableValue);
        }
    }

    public ResultSetDataModelCell<?, ?> getCell() {
        return cell;
    }

    protected int[] getMetrics(int[] metrics) {
        return new int[] {
            (int) Math.max(metrics[0], columnLabel.getPreferredSize().getWidth()),
            (int) Math.max(metrics[1], dataTypeLabel.getPreferredSize().getWidth())};
    }

    protected void adjustMetrics(int[] metrics) {
        columnLabel.setPreferredSize(new Dimension(metrics[0], columnLabel.getHeight()));
        dataTypeLabel.setPreferredSize(new Dimension(metrics[1], valueFieldPanel.getHeight()));
    }

    public Object getEditorValue() throws ParseException {
        DBDataType dataType = cell.getColumnInfo().getDataType();
        Class clazz = dataType.getTypeClass();
        String textValue = valueTextField.getText().trim();
        if (textValue.length() > 0) {
            Object value = cell.getFormatter().parseObject(clazz, textValue);
            return dataType.getNativeType().getDefinition().convert(value);
        } else {
            return null;
        }
    }

    public JTextField getViewComponent() {
        return valueTextField;
    }

    /*********************************************************
     *                     Listeners                         *
     *********************************************************/
    KeyListener keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!e.isConsumed()) {
                ResultSetRecordViewerForm parentForm = ensureParentComponent();
                if (e.getKeyCode() == 38) {//UP
                    parentForm.focusPreviousColumnPanel(ResultSetRecordViewerColumnForm.this);
                    e.consume();
                } else if (e.getKeyCode() == 40) { // DOWN
                    parentForm.focusNextColumnPanel(ResultSetRecordViewerColumnForm.this);
                    e.consume();
                }
            }
        }
    };


    @Override
    public void disposeInner() {
        super.disposeInner();
        cell = null;
    }
}
