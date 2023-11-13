package com.dbn.common.state;


import org.jdom.Element;

public interface PersistentStateElement {
    void readState(Element element);
    void writeState(Element element);

    static <T extends PersistentStateElement> T cloneElement(T source, T target) {
        Element element = new Element("Element");
        source.writeState(element);
        target.readState(element);
        return target;
    }
}
