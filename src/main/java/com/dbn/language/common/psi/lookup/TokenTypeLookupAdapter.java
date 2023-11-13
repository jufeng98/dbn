package com.dbn.language.common.psi.lookup;

import com.dbn.language.common.TokenType;
import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.TokenPsiElement;

import java.util.function.Function;

public class TokenTypeLookupAdapter extends PsiLookupAdapter{
    private final Function<BasePsiElement, TokenType> tokenType;

    public TokenTypeLookupAdapter(Function<BasePsiElement, TokenType> tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public boolean matches(BasePsiElement element) {
        if (element instanceof TokenPsiElement) {
            TokenPsiElement tokenPsiElement = (TokenPsiElement) element;
            return tokenPsiElement.getTokenType() == tokenType.apply(element);
        }
        return false;
    }

    @Override
    public boolean accepts(BasePsiElement element) {
        return true;
    }


}
