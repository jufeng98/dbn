package com.dbn.language.common.element.parser.impl;

import com.dbn.language.common.element.impl.BasicElementType;
import com.dbn.language.common.element.parser.ElementTypeParser;
import com.dbn.language.common.element.parser.ParseResult;
import com.dbn.language.common.element.parser.ParserContext;
import com.dbn.language.common.element.path.ParserNode;

public class BasicElementTypeParser extends ElementTypeParser<BasicElementType> {
    public BasicElementTypeParser(BasicElementType elementType) {
        super(elementType);
    }

    @Override
    public ParseResult parse(ParserNode parentNode, ParserContext context) {
        return ParseResult.noMatch();
    }
}