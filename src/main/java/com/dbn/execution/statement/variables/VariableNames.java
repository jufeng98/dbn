package com.dbn.execution.statement.variables;

import lombok.experimental.UtilityClass;

import static com.dbn.common.util.Strings.toLowerCase;

@UtilityClass
public class VariableNames {

    public static String adjust(String name) {
        name = toLowerCase(name.trim());
        if (name.startsWith(":")) name = name.substring(1);
        return name.intern();
    }
}
