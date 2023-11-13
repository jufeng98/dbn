package com.dbn.execution.statement.variables;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VariableNames {

    public static String adjust(String name) {
        name = name.trim().toLowerCase();
        if (name.startsWith(":")) name = name.substring(1);
        return name.intern();
    }
}
