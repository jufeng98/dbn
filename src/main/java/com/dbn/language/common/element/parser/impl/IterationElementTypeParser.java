package com.dbn.language.common.element.parser.impl;

import com.dbn.language.common.ParseException;
import com.dbn.language.common.TokenType;
import com.dbn.language.common.element.ElementType;
import com.dbn.language.common.element.impl.BasicElementType;
import com.dbn.language.common.element.impl.IterationElementType;
import com.dbn.language.common.element.impl.SequenceElementType;
import com.dbn.language.common.element.impl.TokenElementType;
import com.dbn.language.common.element.parser.*;
import com.dbn.language.common.element.path.ParserNode;
import com.dbn.language.common.element.util.ParseBuilderErrorHandler;
import com.dbn.language.common.element.parser.*;
import com.intellij.lang.PsiBuilder;

import java.util.Set;

public class IterationElementTypeParser extends ElementTypeParser<IterationElementType> {
    public IterationElementTypeParser(IterationElementType elementType) {
        super(elementType);
    }

    @Override
    public ParseResult parse(ParserNode parentNode, ParserContext context) throws ParseException {
        ParserBuilder builder = context.getBuilder();
        ParserNode node = stepIn(parentNode, context);

        ElementType iteratedElementType = elementType.getIteratedElementType();
        TokenElementType[] separatorTokens = elementType.getSeparatorTokens();

        int iterations = 0;
        int matchedTokens = 0;

        //if (shouldParseElement(iteratedElementType, node, context)) {
            ParseResult result = iteratedElementType.getParser().parse(node, context);

            // check first iteration element
            if (result.isMatch()) {
                if (node.isRecursive(node.getStartOffset())) {
                    ParseResultType resultType = matchesMinIterations(iterations) ? result.getType() : ParseResultType.NO_MATCH;
                    return stepOut(node, context, resultType, matchedTokens);
                }
                while (true) {
                    iterations++;
                    // check separator
                    // if not matched just step out
                    PsiBuilder.Marker partialMatchMarker = null;
                    if (separatorTokens != null) {
                        if (elementType.isFollowedBySeparator()) {
                            partialMatchMarker = builder.mark();
                        }

                        ParseResult sepResult = ParseResult.noMatch();
                        for (TokenElementType separatorToken : separatorTokens) {
                            sepResult = separatorToken.getParser().parse(node, context);
                            matchedTokens = matchedTokens + sepResult.getMatchedTokens();
                            if (sepResult.isMatch()) break;
                        }

                        if (sepResult.isNoMatch()) {
                            // if NO_MATCH, no additional separator found, hence then iteration should exit with MATCH
                            ParseResultType resultType =
                                    matchesMinIterations(iterations) ?
                                            matchesIterations(iterations) ?
                                                    result.getType() :
                                                    ParseResultType.PARTIAL_MATCH :
                                            ParseResultType.NO_MATCH;

                            builder.markerDrop(partialMatchMarker);
                            return stepOut(node, context, resultType, matchedTokens);
                        } else {
                            node.setCurrentOffset(builder.getOffset());
                        }
                    }

                    // check consecutive iterated element
                    // if not matched, step out with error

                    result = iteratedElementType.getParser().parse(node, context);

                    if (result.isNoMatch()) {
                        // missing separators permit ending the iteration as valid at any time
                        if (separatorTokens == null) {
                            ParseResultType resultType =
                                    matchesMinIterations(iterations) ?
                                        matchesIterations(iterations) ?
                                            ParseResultType.FULL_MATCH :
                                            ParseResultType.PARTIAL_MATCH :
                                    ParseResultType.NO_MATCH;
                            return stepOut(node, context, resultType, matchedTokens);
                        } else {
                            if (matchesMinIterations(iterations)) {
                                if (elementType.isFollowedBySeparator()) {
                                    builder.markerRollbackTo(partialMatchMarker);
                                    return stepOut(node, context, ParseResultType.FULL_MATCH, matchedTokens);
                                } else {
                                    builder.markerDrop(partialMatchMarker);
                                }

                                boolean exit = advanceLexerToNextLandmark(node, false, context);
                                if (exit){
                                    return stepOut(node, context, ParseResultType.PARTIAL_MATCH, matchedTokens);
                                }
                            } else {
                                builder.markerDrop(partialMatchMarker);
                                return stepOut(node, context, ParseResultType.NO_MATCH, matchedTokens);
                            }
                        }
                    } else {
                        builder.markerDrop(partialMatchMarker);
                        matchedTokens = matchedTokens + result.getMatchedTokens();
                    }
                }
            }
        //}
        return stepOut(node, context, ParseResultType.NO_MATCH, matchedTokens);
    }

    private boolean advanceLexerToNextLandmark(ParserNode parentNode, boolean lenient, ParserContext context) {
        ParserBuilder builder = context.getBuilder();

        PsiBuilder.Marker marker = builder.mark();
        ElementType iteratedElementType = elementType.getIteratedElementType();
        TokenElementType[] separatorTokens = elementType.getSeparatorTokens();

        if (!lenient) {
            Set<TokenType> expectedTokens = iteratedElementType.getLookupCache().captureFirstPossibleTokens(context.reset());
            ParseBuilderErrorHandler.updateBuilderError(expectedTokens, context);
        }
        boolean advanced = false;
        BasicElementType unknownElementType = getElementBundle().getUnknownElementType();
        while (!builder.eof()) {
            TokenType token = builder.getToken();
            if (token == null || token.isChameleon())  break;

            if (token.isParserLandmark()) {
                if (separatorTokens != null) {
                    for (TokenElementType separatorToken : separatorTokens) {
                        if (separatorToken.getLookupCache().containsToken(token)) {
                            builder.markerDone(marker, unknownElementType);
                            return false;
                        }
                    }
                }

                ParserNode parseNode = parentNode;
                while (parseNode != null) {
                    if (parseNode.getElement() instanceof SequenceElementType) {
                        SequenceElementType sequenceElementType = (SequenceElementType) parseNode.getElement();
                        int index = parseNode.getCursorPosition();
                        if (!iteratedElementType.getLookupCache().containsToken(token) && sequenceElementType.containsLandmarkTokenFromIndex(token, index + 1)) {
                            if (advanced || !lenient) {
                                builder.markerDone(marker, unknownElementType);
                            } else {
                                builder.markerRollbackTo(marker);
                            }
                            return true;
                        }

                    }
                    parseNode = parseNode.getParent();
                }
            }
            builder.advance();
            advanced = true;
        }
        if (advanced || !lenient)
            builder.markerDone(marker, unknownElementType); else
            builder.markerRollbackTo(marker);
        return true;
    }

    @Deprecated
    private boolean matchesMinIterations(int iterations) {
        return elementType.getMinIterations() <= iterations;
    }

    @Deprecated
    private boolean matchesIterations(int iterations) {
        int[]elementsCountVariants = elementType.getElementsCountVariants();
        if (elementsCountVariants != null) {
            for (int elementsCountVariant: elementsCountVariants) {
                if (elementsCountVariant == iterations) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean matchesIterationConstraints(int iterations) {
        if (elementType.getMinIterations() <= iterations) {
            int[]elementsCountVariants = elementType.getElementsCountVariants();
            if (elementsCountVariants != null) {
                for (int elementsCountVariant: elementsCountVariants) {
                    if (elementsCountVariant == iterations) {
                        return true;
                    }
                }
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
