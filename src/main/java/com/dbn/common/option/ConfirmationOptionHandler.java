package com.dbn.common.option;


import com.dbn.common.icon.Icons;
import com.dbn.common.options.PersistentConfiguration;
import com.dbn.common.options.setting.Settings;
import com.dbn.common.util.Titles;
import com.intellij.openapi.ui.Messages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

@Getter
@Setter
@EqualsAndHashCode
public class ConfirmationOptionHandler implements DoNotAskOption, PersistentConfiguration{
    private final String configName;
    private final String title;
    private final String message;
    private boolean confirm;

    public ConfirmationOptionHandler(String configName, String title, String message, boolean defaultKeepAsking) {
        this.configName = configName;
        this.title = title;
        this.message = message;
        this.confirm = defaultKeepAsking;
    }

    @Override
    public boolean isToBeShown() {
        return true;
    }

    @Override
    public void setToBeShown(boolean keepAsking, int selectedIndex) {
        this.confirm = keepAsking;
    }

    @Override
    public boolean canBeHidden() {
        return true;
    }

    @Override
    public boolean shouldSaveOptionsOnCancel() {
        return false;
    }

    @NotNull
    @Override
    public String getDoNotShowMessage() {
        return "Do not ask again";
    }

    public boolean resolve(Object ... messageArgs) {
        if (!confirm) return true;

        int optionIndex = Messages.showDialog(
                MessageFormat.format(message, messageArgs),
                Titles.signed(title),
                new String[]{"Yes", "No"}, 0, Icons.DIALOG_QUESTION, this);
        return optionIndex == 0;
    }

    /*******************************************************
     *              PersistentConfiguration                *
     *******************************************************/
    @Override
    public void readConfiguration(Element element) {
        confirm = Settings.getBoolean(element, configName, confirm);
    }

    @Override
    public void writeConfiguration(Element element) {
        Settings.setBoolean(element, configName, confirm);
    }
}
