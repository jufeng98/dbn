package com.dbn.common.options.setting;

import com.dbn.common.options.PersistentConfiguration;
import org.jdom.Element;

import javax.swing.*;

public class BooleanSetting extends Setting<Boolean, JToggleButton> implements PersistentConfiguration {
    public BooleanSetting(String name, Boolean value) {
        super(name, value);
    }
    
    @Override
    public void readConfiguration(Element parent) {
        setValue(Settings.getBoolean(parent, getName(), this.value()));
    }

    public void readConfigurationAttribute(Element parent) {
        setValue(Settings.booleanAttribute(parent, getName(), this.value()));
    }

    @Override
    public void writeConfiguration(Element parent) {
        Settings.setBoolean(parent, getName(), this.value());
    }

    public void writeConfigurationAttribute(Element parent) {
        Settings.setBooleanAttribute(parent, getName(), this.value());
    }


    @Override
    public boolean to(JToggleButton checkBox) {
        return setValue(checkBox.isSelected());
    }
    
    @Override
    public void from(JToggleButton checkBox) {
        checkBox.setSelected(value());
    }
}
