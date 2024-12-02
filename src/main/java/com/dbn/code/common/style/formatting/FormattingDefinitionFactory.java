package com.dbn.code.common.style.formatting;

import org.jdom.Element;

public class FormattingDefinitionFactory {
    @SuppressWarnings("unused")
    public static FormattingDefinition cloneDefinition(FormattingDefinition attributes) {
        if (attributes != null && !attributes.isEmpty()) {
            return new FormattingDefinition(attributes);
        }
        return null;
    }

    public static FormattingDefinition loadDefinition(Element element) {
        if (element != null) {
            FormattingDefinition attributes = new FormattingDefinition(element);
            if (!attributes.isEmpty()) {
                return attributes;
            }
        }
        return null;
    }

    public static FormattingDefinition mergeDefinitions(FormattingDefinition definition, FormattingDefinition defaultDefinition) {
        if (definition == null) {
            if (defaultDefinition != null && !defaultDefinition.isEmpty()) {
                definition = new FormattingDefinition(defaultDefinition);
            }
        } else if (defaultDefinition != null){
            definition.merge(defaultDefinition);
        }
        return definition;
    }

}
