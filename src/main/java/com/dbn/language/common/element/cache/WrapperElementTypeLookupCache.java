package com.dbn.language.common.element.cache;

import com.dbn.language.common.element.impl.LeafElementType;
import com.dbn.language.common.element.impl.TokenElementType;
import com.dbn.language.common.element.impl.WrapperElementType;
import com.dbn.language.common.TokenType;
import com.dbn.language.common.element.ElementType;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WrapperElementTypeLookupCache extends ElementTypeLookupCache<WrapperElementType> {

    public WrapperElementTypeLookupCache(WrapperElementType elementType) {
        super(elementType);
    }

/*
    @Override
    boolean initAsFirstPossibleLeaf(LeafElementType leaf, ElementType source) {
        ElementTypeLookupCache startTokenLC = getElementType().getBeginTokenElement().getLookupCache();
        ElementTypeLookupCache wrappedTokenLC = getElementType().getWrappedElement().getLookupCache();
        return startTokenLC.couldStartWithLeaf(leaf) ||
               (*/
/*getElementType().isWrappingOptional() && *//*
wrappedTokenLC.couldStartWithLeaf(leaf));
    }

    @Override
    boolean initAsFirstRequiredLeaf(LeafElementType leaf, ElementType source) {
        ElementTypeLookupCache startTokenLC = getElementType().getBeginTokenElement().getLookupCache();
        return startTokenLC.shouldStartWithLeaf(leaf);
    }
*/

    @Override
    public Set<LeafElementType> captureFirstPossibleLeafs(ElementLookupContext context, @Nullable Set<LeafElementType> bucket) {
        bucket = super.captureFirstPossibleLeafs(context, bucket);
        bucket = initBucket(bucket);
        bucket.add(elementType.getBeginTokenElement());
        return bucket;
    }

    @Override
    public Set<TokenType> captureFirstPossibleTokens(ElementLookupContext context, @Nullable Set<TokenType> bucket) {
        bucket = super.captureFirstPossibleTokens(context, bucket);
        bucket = initBucket(bucket);
        bucket.add(elementType.getBeginTokenElement().getTokenType());
        return bucket;
    }

    @Override
    public boolean containsToken(TokenType tokenType) {
        return getBeginTokenElement().getTokenType() == tokenType ||
                getEndTokenElement().getTokenType() == tokenType ||
                getWrappedElement().getLookupCache().containsToken(tokenType);
    }

    @Override
    public boolean containsLeaf(LeafElementType leafElementType) {
        return getBeginTokenElement() == leafElementType ||
                getEndTokenElement() == leafElementType ||
                getWrappedElement().getLookupCache().containsLeaf(leafElementType);
    }

    @Override
    public Set<TokenType> getFirstPossibleTokens() {
        Set<TokenType> tokenTypes = initBucket(null);
        tokenTypes.add(getBeginTokenElement().getTokenType());
        getWrappedElement().getLookupCache().captureFirstPossibleTokens(tokenTypes);
        return tokenTypes;
    }

    @Override
    public Set<TokenType> getFirstRequiredTokens() {
        Set<TokenType> tokenTypes = initBucket(null);
        tokenTypes.add(getBeginTokenElement().getTokenType());
        return tokenTypes;
    }

    @Override
    public boolean couldStartWithLeaf(LeafElementType elementType) {
        return getBeginTokenElement() == elementType || getWrappedElement().getLookupCache().couldStartWithLeaf(elementType);
    }

    @Override
    public boolean shouldStartWithLeaf(LeafElementType elementType) {
        return getBeginTokenElement() == elementType;
    }

    @Override
    public boolean couldStartWithToken(TokenType tokenType) {
        return getBeginTokenElement().getLookupCache().couldStartWithToken(tokenType) || getWrappedElement().getLookupCache().couldStartWithToken(tokenType);
    }

    private ElementType getWrappedElement() {
        return elementType.getWrappedElement();
    }

    @Override
    public Set<LeafElementType> getFirstPossibleLeafs() {
        Set<LeafElementType> firstPossibleLeafs = initBucket(null);
        firstPossibleLeafs.add(getBeginTokenElement());
        return firstPossibleLeafs;
    }

    @Override
    public Set<LeafElementType> getFirstRequiredLeafs() {
        Set<LeafElementType> firstRequiredLeafs = initBucket(null);
        firstRequiredLeafs.add(getBeginTokenElement());
        return firstRequiredLeafs;
    }

    @Override
    public boolean isFirstPossibleLeaf(LeafElementType elementType) {
        return getBeginTokenElement() == elementType;
    }

    @Override
    public boolean isFirstRequiredLeaf(LeafElementType elementType) {
        return isFirstPossibleLeaf(elementType);
    }

    @Override
    public boolean startsWithIdentifier() {
        return false;
    }

    @Override
    public boolean isFirstPossibleToken(TokenType tokenType) {
        return couldStartWithToken(tokenType);
    }

    @Override
    public boolean isFirstRequiredToken(TokenType tokenType) {
        return getBeginTokenElement().getTokenType() == tokenType;
    }

    private TokenElementType getBeginTokenElement() {
        return elementType.getBeginTokenElement();
    }

    private TokenElementType getEndTokenElement() {
        return this.elementType.getEndTokenElement();
    }
}