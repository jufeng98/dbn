package com.dbn.code.common.completion.options.general;

import com.dbn.code.common.completion.options.CodeCompletionSettings;
import com.dbn.code.common.completion.options.general.ui.CodeCompletionFormatSettingsForm;
import com.dbn.common.options.BasicConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.options.setting.Settings.getBoolean;
import static com.dbn.common.options.setting.Settings.setBoolean;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CodeCompletionFormatSettings extends BasicConfiguration<CodeCompletionSettings, CodeCompletionFormatSettingsForm> {
    private boolean enforceCodeStyleCase = true;

    public CodeCompletionFormatSettings(CodeCompletionSettings parent) {
        super(parent);
    }

    /****************************************************
     *                   Configuration                  *
     ****************************************************/
   @Override
   @NotNull
   public CodeCompletionFormatSettingsForm createConfigurationEditor() {
       return new CodeCompletionFormatSettingsForm(this);
   }

    @Override
    public String getConfigElementName() {
        return "format";
    }

    @Override
    public void readConfiguration(Element element) {
        enforceCodeStyleCase = getBoolean(element, "enforce-code-style-case", enforceCodeStyleCase);
    }

    @Override
    public void writeConfiguration(Element element) {
        setBoolean(element, "enforce-code-style-case", enforceCodeStyleCase);
    }

}
