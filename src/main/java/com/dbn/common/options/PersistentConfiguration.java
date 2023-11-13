package com.dbn.common.options;

import com.intellij.openapi.options.ConfigurationException;
import org.jdom.Element;

public interface PersistentConfiguration {
    void readConfiguration(Element element);
    void writeConfiguration(Element element);

    default void validate() throws ConfigurationException {};
}