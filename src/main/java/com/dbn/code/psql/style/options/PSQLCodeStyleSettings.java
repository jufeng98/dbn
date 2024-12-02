package com.dbn.code.psql.style.options;

import com.dbn.code.psql.style.options.ui.PSQLCodeStyleSettingsEditorForm;
import com.dbn.code.common.style.options.CodeStyleCaseSettings;
import com.dbn.code.common.style.options.CodeStyleFormattingSettings;
import com.dbn.code.common.style.options.DBLCodeStyleSettings;
import com.dbn.common.icon.Icons;
import com.dbn.language.psql.PSQLLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PSQLCodeStyleSettings extends DBLCodeStyleSettings<DBLCodeStyleSettings<?, ?>, PSQLCodeStyleSettingsEditorForm> {

    PSQLCodeStyleSettings(DBLCodeStyleSettings<?, ?> parent) {
        super(parent);
    }

    @Override
    @Nullable
    public Icon getIcon() {
        return Icons.FILE_PLSQL;
    }

    @Override
    public String getDisplayName() {
        return "PL/SQL";
    }

    @Override
    protected CodeStyleCaseSettings createCaseSettings(DBLCodeStyleSettings<?, ?> parent) {
        return new PSQLCodeStyleCaseSettings(parent);
    }

    @Override
    protected CodeStyleFormattingSettings createAttributeSettings(DBLCodeStyleSettings<?, ?> parent) {
        return new PSQLCodeStyleFormattingSettings(parent);
    }

    @Override
    protected String getElementName() {
        return PSQLLanguage.ID;
    }

    /*********************************************************
    *                     Configuration                     *
    *********************************************************/
    @Override
    @NotNull
    public PSQLCodeStyleSettingsEditorForm createConfigurationEditor() {
        return new PSQLCodeStyleSettingsEditorForm(this);
    }


}