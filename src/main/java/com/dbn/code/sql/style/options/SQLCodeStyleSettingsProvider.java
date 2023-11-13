package com.dbn.code.sql.style.options;

import com.dbn.language.sql.SQLLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SQLCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    @Override
    public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings codeStyleSettings) {
        return new SQLCodeStyleSettingsWrapper(codeStyleSettings);
    }

    @Override
    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings modelSettings) {
        SQLCodeStyleSettingsWrapper settingsProvider = settings.getCustomSettings(SQLCodeStyleSettingsWrapper.class);
        return settingsProvider.getSettings();
    }

    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings) {
        SQLCodeStyleSettingsWrapper settingsProvider = settings.getCustomSettings(SQLCodeStyleSettingsWrapper.class);
        return settingsProvider.getSettings();
    }

    @Override
    public String getConfigurableDisplayName() {
        return "SQL (DBN)";
    }

    @Nullable
    @Override
    public Language getLanguage() {
        return SQLLanguage.INSTANCE;
    }
}
