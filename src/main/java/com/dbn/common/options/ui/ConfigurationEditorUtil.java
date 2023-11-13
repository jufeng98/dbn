package com.dbn.common.options.ui;

import com.dbn.common.util.Strings;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class ConfigurationEditorUtil {
    public static int validateIntegerValue(@NotNull JTextField inputField, @NotNull String name, boolean required, int min, int max, @Nullable String hint) throws ConfigurationException {
        try {

            String value = inputField.getText();
            if (required && Strings.isEmpty(value)) {
                String message = "Input value for \"" + name + "\" must be specified";
                throw new ConfigurationException(message, "Invalid config value");
            }

            if (Strings.isNotEmpty(value)) {
                int integer = Integer.parseInt(value);
                if (min > integer || max < integer) throw new NumberFormatException("Number not in range");
                return integer;
            }
            return 0;
        } catch (NumberFormatException e) {
            conditionallyLog(e);
            inputField.grabFocus();
            inputField.selectAll();
            String message = "Input value for \"" + name + "\" must be an integer between " + min + " and " + max + ".";
            if (hint != null) {
                message = message + " " + hint;
            }
            throw new ConfigurationException(message, "Invalid config value");
        }
    }

    public static String validateStringValue(@NotNull JTextField inputField, @NotNull String name, boolean required) throws ConfigurationException {
        String value = inputField.getText().trim();
        if (required && value.length() == 0) {
            String message = "Input value for \"" + name + "\" must be specified";
            throw new ConfigurationException(message, "Invalid config value");
        }
        return value;
    }
    
}
