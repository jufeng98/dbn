package com.dbn.code.sql.style.options;

import com.dbn.language.sql.SQLLanguage;
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
public class SQLCodeStyleSettingsWrapper extends CustomCodeStyleSettings {
    private final SQLCodeStyleSettings settings;

    SQLCodeStyleSettingsWrapper(CodeStyleSettings container) {
        super(SQLLanguage.ID, container);
        settings = new SQLCodeStyleSettings(null);
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
