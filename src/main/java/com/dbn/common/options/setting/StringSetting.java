package com.dbn.common.options.setting;

import com.dbn.common.util.Strings;
import com.dbn.common.options.PersistentConfiguration;
import com.intellij.openapi.options.ConfigurationException;
import org.jdom.Element;

import javax.swing.text.JTextComponent;

public class StringSetting extends Setting<String, JTextComponent> implements PersistentConfiguration {
    public StringSetting(String name, String value) {
        super(name, value);
    }
    
    @Override
    public void readConfiguration(Element parent) {
        setValue(Settings.getString(parent, getName(), this.value()));
    }

    @Override
    public void writeConfiguration(Element parent) {
        Settings.setString(parent, getName(), this.value());
    }

    @Override
    public boolean to(JTextComponent component) throws ConfigurationException {
        return setValue(Strings.trim(component.getText()));
    }

    @Override
    public void from(JTextComponent component) {
        component.setText(value());
    }

}
