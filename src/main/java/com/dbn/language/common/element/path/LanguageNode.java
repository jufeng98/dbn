package com.dbn.language.common.element.path;

import com.dbn.common.path.Node;
import com.dbn.language.common.element.ElementType;

public interface LanguageNode extends Node<ElementType> {

    @Override
    LanguageNode getParent();

    int getIndexInParent();
}
