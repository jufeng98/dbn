package com.dbn.common.util;

import com.intellij.openapi.util.JDOMUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.InputStream;

@Slf4j
@UtilityClass
public final class XmlContents {

    public static Element fileToElement(Class clazz, String fileName) throws Exception {
        try (InputStream inputStream = clazz.getResourceAsStream(fileName)){
            return streamToElement(inputStream);
        }
    }

    public static Element streamToElement(InputStream inputStream) throws Exception{
        return JDOMUtil.load(inputStream);
    }

    public static Document fileToDocument(Class clazz, String fileName) throws Exception {
        try (InputStream inputStream = clazz.getResourceAsStream(fileName)){
            return streamToDocument(inputStream);
        }
    }

    public static Document streamToDocument(InputStream inputStream) throws Exception{
        SAXBuilder builder = new SAXBuilder();
        return builder.build(inputStream);
    }

}
