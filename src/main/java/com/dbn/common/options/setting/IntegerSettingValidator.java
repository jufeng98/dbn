package com.dbn.common.options.setting;

import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

public class IntegerSettingValidator implements SettingValidator<IntegerSetting>{
    private String fieldName;
    private String hint;
    private int minValue;
    private int maxValue;
    
    @Override
    public void validate(IntegerSetting setting) throws ConfigurationException {
        
    }

    public static int parseIntegerInputValue(JTextField inputField, String name, int min, int max, String hint) throws ConfigurationException {
        try {
            int integer = Integer.parseInt(inputField.getText());
            if (min > integer || max < integer) throw new NumberFormatException("Number not in range");
            return integer;
        } catch (NumberFormatException e) {
            inputField.grabFocus();
            inputField.selectAll();
            String message = "Input value for \"" + name + "\" must be an integer between " + min + " and " + max + ".";
            if (hint != null) {
                message = message + " " + hint;
            }
            throw new ConfigurationException(message, "Invalid config value");
        }
    }
}
