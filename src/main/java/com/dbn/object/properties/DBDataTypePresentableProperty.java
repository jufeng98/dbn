package com.dbn.object.properties;

import com.dbn.data.type.DBDataType;
import com.dbn.object.DBType;
import com.intellij.pom.Navigatable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.swing.Icon;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class DBDataTypePresentableProperty extends PresentableProperty{
    private final DBDataType dataType;
    private String name = "Data type";

    public DBDataTypePresentableProperty(String name, DBDataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public DBDataTypePresentableProperty(DBDataType dataType) {
        this.dataType = dataType;
    }

    @Override
    public String getValue() {
        return dataType.getQualifiedName();
    }

    @Override
    public Icon getIcon() {
        DBType declaredType = dataType.getDeclaredType();
        return declaredType == null ? null : declaredType.getIcon();
    }

    @Override
    public Navigatable getNavigatable() {
        return dataType.getDeclaredType();
    }
}
