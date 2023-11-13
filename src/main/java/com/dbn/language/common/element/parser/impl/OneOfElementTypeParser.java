package com.dbn.language.common.element.parser.impl;

import com.dbn.language.common.ParseException;
import com.dbn.language.common.TokenType;
import com.dbn.language.common.element.impl.ElementTypeRef;
import com.dbn.language.common.element.impl.OneOfElementType;
import com.dbn.language.common.element.parser.*;
import com.dbn.language.common.element.path.ParserNode;
import com.dbn.language.common.element.parser.*;

public class OneOfElementTypeParser extends ElementTypeParser<OneOfElementType> {

    public OneOfElementTypeParser(OneOfElementType elementType) {
        super(elementType);
    }

    @Override
    public ParseResult parse(ParserNode parentNode, ParserContext context) throws ParseException {
        ParserBuilder builder = context.getBuilder();
        ParserNode node = stepIn(parentNode, context);

        elementType.sort();
        TokenType token = builder.getToken();

        if (token != null && !token.isChameleon()) {
            ElementTypeRef element = elementType.getFirstChild();
            while (element != null) {
                if (context.check(element) && shouldParseElement(element.getElementType(), node, context)) {
                    ParseResult result = element.getParser().parse(node, context);

                    if (result.isMatch()) {
                        return stepOut(node, context, result.getType(), result.getMatchedTokens());
                    }
                }
                element = element.getNext();
            }
        }
        return stepOut(node, context, ParseResultType.NO_MATCH, 0);
    }
}