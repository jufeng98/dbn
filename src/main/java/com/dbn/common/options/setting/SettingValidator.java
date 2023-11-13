package com.dbn.common.options.setting;

import com.intellij.openapi.options.ConfigurationException;

public interface SettingValidator<T extends Setting> {
    void validate(T setting) throws ConfigurationException;
}
