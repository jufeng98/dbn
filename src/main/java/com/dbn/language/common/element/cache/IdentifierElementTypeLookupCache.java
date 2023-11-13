package com.dbn.language.common.element.cache;

import com.dbn.language.common.element.impl.IdentifierElementType;
import com.dbn.language.common.SharedTokenTypeBundle;
import com.dbn.language.common.TokenType;

import java.util.Set;

public class IdentifierElementTypeLookupCache extends LeafElementTypeLookupCache<IdentifierElementType>{
    public IdentifierElementTypeLookupCache(IdentifierElementType elementType) {
        super(elementType);
    }

    @Override
    public boolean couldStartWithToken(TokenType tokenType) {
        return tokenType.isIdentifier();
    }

    @Override
    public boolean containsToken(TokenType tokenType) {
        SharedTokenTypeBundle sharedTokenTypes = getSharedTokenTypes();
        return sharedTokenTypes.getIdentifier() == tokenType || sharedTokenTypes.getQuotedIdentifier() == tokenType;
    }

    @Override
    public Set<TokenType> getFirstPossibleTokens() {
        SharedTokenTypeBundle sharedTokenTypes = getSharedTokenTypes();
        return sharedTokenTypes.getIdentifierTokens();
    }

    @Override
    public boolean isFirstPossibleToken(TokenType tokenType) {
        return tokenType.isIdentifier();
    }

    @Override
    public boolean isFirstRequiredToken(TokenType tokenType) {
        return tokenType.isIdentifier();
    }

    @Override
    public void captureFirstPossibleTokens(Set<TokenType> bucket) {
        SharedTokenTypeBundle sharedTokenTypes = getSharedTokenTypes();
        bucket.add(sharedTokenTypes.getIdentifier());
        bucket.add(sharedTokenTypes.getQuotedIdentifier());
    }

    @Override
    public boolean startsWithIdentifier() {
        return true;
    }



}