package com.dbn.common.properties;

public class KeyValueProperty {
    private String key;
    private String value;

    public KeyValueProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValueProperty() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
