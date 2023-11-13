package com.dbn.ddl.ui;

import com.dbn.common.file.VirtualFileInfo;
import com.dbn.common.file.ui.FileListCellRenderer;
import com.dbn.common.text.TextContent;
import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.form.DBNHeaderForm;
import com.dbn.common.ui.form.DBNHintForm;
import com.dbn.object.common.DBSchemaObject;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectDDLFileForm extends DBNFormBase {
    private JPanel mainPanel;
    private JList<VirtualFileInfo> filesList;
    private JPanel headerPanel;
    private JCheckBox doNotPromptCheckBox;
    private JPanel hintPanel;

    SelectDDLFileForm(DBNDialog<?> parent, DBSchemaObject object, List<VirtualFileInfo> fileInfos, TextContent hint, boolean isFileOpenEvent) {
        super(parent);
        DBNHeaderForm headerForm = new DBNHeaderForm(this, object);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);

        DBNHintForm hintForm = new DBNHintForm(this, hint, null, true);
        hintPanel.add(hintForm.getComponent(), BorderLayout.CENTER);

        DefaultListModel<VirtualFileInfo> listModel = new DefaultListModel<>();
        for (VirtualFileInfo fileInfo : fileInfos) {
            listModel.addElement(fileInfo);
        }
        filesList.setModel(listModel);
        filesList.setCellRenderer(new FileListCellRenderer());
        filesList.setSelectedIndex(0);

        if (!isFileOpenEvent) mainPanel.remove(doNotPromptCheckBox);
    }

    public List<VirtualFileInfo> getSelection() {
        return filesList.getSelectedValuesList();
    }

    public void selectAll() {
        filesList.setSelectionInterval(0, filesList.getModel().getSize() -1);
    }

    public void selectNone() {
        filesList.clearSelection();
    }

    public boolean isDoNotPromptSelected() {
        return doNotPromptCheckBox.isSelected();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
