package com.dbn.code.psql.style.options;

import com.dbn.language.psql.PSQLLanguage;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class PSQLCodeStyleSettingsWrapper extends CustomCodeStyleSettings {
    private final PSQLCodeStyleSettings settings;

    PSQLCodeStyleSettingsWrapper(CodeStyleSettings container) {
        super(PSQLLanguage.ID, container);
        settings = new PSQLCodeStyleSettings(null);
    }

    @Override
    public void readExternal(Element parentElement) throws InvalidDataException {
        settings.readConfiguration(parentElement);
    }

    @Override
    public void writeExternal(Element parentElement, @NotNull CustomCodeStyleSettings parentSettings) throws WriteExternalException {
        settings.writeConfiguration(parentElement);
    }
}