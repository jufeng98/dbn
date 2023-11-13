package com.dbn.language.common.element.path;

import com.dbn.common.path.NodeBase;
import com.dbn.language.common.element.ElementType;
import com.dbn.language.common.element.util.ElementTypeAttribute;
import lombok.Getter;

@Getter
public class LanguageNodeBase extends NodeBase<ElementType> implements LanguageNode {
    public LanguageNodeBase(ElementType elementType, LanguageNodeBase parent) {
        super(elementType, parent);
    }

    @Override
    public LanguageNodeBase getParent() {
        return (LanguageNodeBase) super.getParent();
    }

    public LanguageNode getParent(ElementTypeAttribute attribute) {
        LanguageNode pathNode = this;
        while (pathNode != null) {
            if (pathNode.getElement().is(attribute)) {
                return pathNode;
            }
            pathNode = pathNode.getParent();
        }
        return null;

    }

    public int getIndexInParent() {
        return this.getElement().getIndexInParent(this);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        LanguageNode parent = this;
        while (parent != null) {
            buffer.insert(0, '/');
            buffer.insert(0, parent.getElement().getId());
            parent = parent.getParent();
        }
        return buffer.toString();
    }
}
