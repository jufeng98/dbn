package com.dbn.code.psql.style.options;

import com.dbn.language.psql.PSQLLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PSQLCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    @Override
    public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings codeStyleSettings) {
        return new PSQLCodeStyleSettingsWrapper(codeStyleSettings);
    }

    @Override
    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings modelSettings) {
        PSQLCodeStyleSettingsWrapper settingsProvider = settings.getCustomSettings(PSQLCodeStyleSettingsWrapper.class);
        return settingsProvider.getSettings();
    }

    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings) {
        PSQLCodeStyleSettingsWrapper settingsProvider = settings.getCustomSettings(PSQLCodeStyleSettingsWrapper.class);
        return settingsProvider.getSettings();
    }

    @Override
    public String getConfigurableDisplayName() {
        return "PL/SQL (DBN)";
    }

    @Nullable
    @Override
    public Language getLanguage() {
        return PSQLLanguage.INSTANCE;
    }
}