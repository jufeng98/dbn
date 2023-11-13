package com.dbn.data.type.ui;

import com.dbn.code.common.style.options.CodeStyleCaseOption;
import com.dbn.code.common.style.options.CodeStyleCaseSettings;
import com.dbn.code.psql.style.PSQLCodeStyle;
import com.dbn.common.ui.misc.DBNButton;
import com.dbn.connection.ConnectionHandler;
import com.dbn.data.editor.ui.BasicListPopupValuesProvider;
import com.dbn.data.editor.ui.TextFieldWithPopup;
import com.dbn.data.type.DataTypeDefinition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataTypeEditor extends TextFieldWithPopup {
    public DataTypeEditor(ConnectionHandler connection) {
        super(connection.getProject());
        CodeStyleCaseSettings caseSettings = PSQLCodeStyle.caseSettings(getProject());
        CodeStyleCaseOption caseOption = caseSettings.getObjectCaseOption();

        List<DataTypeDefinition> nativeDataTypes = connection.getInterfaces().getNativeDataTypes().list();
        List<String> nativeDataTypeNames = new ArrayList<>();
        for (DataTypeDefinition nativeDataType : nativeDataTypes) {
            String typeName = nativeDataType.getName();
            typeName = caseOption.format(typeName);
            nativeDataTypeNames.add(typeName);
        }
        BasicListPopupValuesProvider valuesProvider = new BasicListPopupValuesProvider("Native Data Types", nativeDataTypeNames);
        createValuesListPopup(valuesProvider, true);
    }


    public String getDataTypeRepresentation() {
        return getTextField().getText();
    }

    @Override
    public void customizeTextField(JTextField textField) {
        Dimension preferredSize = textField.getPreferredSize();
        preferredSize = new Dimension(120, preferredSize.height);
        getTextField().setPreferredSize(preferredSize);
    }

    @Override
    public void customizeButton(DBNButton button) {
        super.customizeButton(button);
    }
}
