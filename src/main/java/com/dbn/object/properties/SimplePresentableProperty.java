package com.dbn.object.properties;

import com.intellij.pom.Navigatable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.swing.Icon;

@Getter
@EqualsAndHashCode(callSuper = false)
public class SimplePresentableProperty extends PresentableProperty{
    private final String name;
    private final String value;
    private final Icon icon;

    public SimplePresentableProperty(String name, String value, Icon icon) {
        this.name = name;
        this.value = value;
        this.icon = icon;
    }

    public SimplePresentableProperty(String name, String value) {
        this(name, value, null);
    }

    @Override
    public Navigatable getNavigatable() {
        return null;
    }
}
