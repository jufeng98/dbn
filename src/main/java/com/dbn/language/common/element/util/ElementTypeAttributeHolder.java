package com.dbn.language.common.element.util;

import com.dbn.common.property.PropertyHolderBase;

import java.util.Objects;
import java.util.StringTokenizer;

public class ElementTypeAttributeHolder extends PropertyHolderBase.LongStore<ElementTypeAttribute> {

    @Override
    protected ElementTypeAttribute[] properties() {
        return ElementTypeAttribute.VALUES;
    }

    public ElementTypeAttributeHolder(String definition) throws ElementTypeDefinitionException {
        StringTokenizer tokenizer = new StringTokenizer(definition, ",");
        while (tokenizer.hasMoreTokens()) {
            String attributeName = tokenizer.nextToken().trim();
            boolean found = false;
            for (ElementTypeAttribute attribute : ElementTypeAttribute.values()) {
                if (Objects.equals(attribute.getName(), attributeName)) {
                    set(attribute, true);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ElementTypeDefinitionException("Invalid element type attribute '" + attributeName + "'");
            }
        }
    }
}
