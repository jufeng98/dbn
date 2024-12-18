package com.dbn.code.sql.style.options;

import com.dbn.code.sql.style.options.ui.SQLCodeStyleSettingsEditorForm;
import com.dbn.code.common.style.options.CodeStyleCaseSettings;
import com.dbn.code.common.style.options.CodeStyleFormattingSettings;
import com.dbn.code.common.style.options.DBLCodeStyleSettings;
import com.dbn.common.icon.Icons;
import com.dbn.language.sql.SQLLanguage;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SQLCodeStyleSettings extends DBLCodeStyleSettings<DBLCodeStyleSettings<?, ?>, SQLCodeStyleSettingsEditorForm> {

    SQLCodeStyleSettings(DBLCodeStyleSettings<?, ?> parent) {
        super(parent);
    }

    @Override
    @Nls
    public String getDisplayName() {
        return "SQL";
    }

    @Override
    @Nullable
    public Icon getIcon() {
        return Icons.FILE_SQL;
    }

    @Override
    protected CodeStyleCaseSettings createCaseSettings(DBLCodeStyleSettings<?, ?> parent) {
        return new SQLCodeStyleCaseSettings(parent);
    }

    @Override
    protected CodeStyleFormattingSettings createAttributeSettings(DBLCodeStyleSettings<?, ?> parent) {
        return new SQLCodeStyleFormattingSettings(parent);
    }

    @Override
    protected String getElementName() {
        return SQLLanguage.ID;
    }

    /*********************************************************
    *                     Configuration                     *
    *********************************************************/
    @Override
    @NotNull
    public SQLCodeStyleSettingsEditorForm createConfigurationEditor() {
        return new SQLCodeStyleSettingsEditorForm(this);
    }
}
