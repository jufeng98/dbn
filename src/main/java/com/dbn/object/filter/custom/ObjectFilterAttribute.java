package com.dbn.object.filter.custom;

import com.dbn.code.common.lookup.CodeCompletionLookupItem;
import com.intellij.codeInsight.lookup.LookupItem;
import lombok.Value;

@Value
public class ObjectFilterAttribute {
    private final Class type;
    private final String name;
    private final String description;

    public LookupItem asLookupItem() {
        return new CodeCompletionLookupItem(this, null, name, description, false);
    }

    public Object getTestValue() {
        if (boolean.class.isAssignableFrom(type)) return false;
        if (Boolean.class.isAssignableFrom(type)) return false;
        if (String.class.isAssignableFrom(type)) return "";
        if (Short.class.isAssignableFrom(type)) return 0;
        if (Integer.class.isAssignableFrom(type)) return 0;
        if (Long.class.isAssignableFrom(type)) return 0L;
        if (Float.class.isAssignableFrom(type)) return 0.0f;
        if (Double.class.isAssignableFrom(type)) return 0.0;
        if (Byte.class.isAssignableFrom(type)) return (byte) 0;
        if (Character.class.isAssignableFrom(type)) return ' ';
        if (Enum.class.isAssignableFrom(type)) return null;
        return null;
    }

}
