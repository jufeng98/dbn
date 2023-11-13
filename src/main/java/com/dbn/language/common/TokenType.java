package com.dbn.language.common;

import com.dbn.language.common.element.TokenPairTemplate;
import com.dbn.code.common.style.formatting.FormattingDefinition;
import com.dbn.common.index.Indexable;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TokenType extends Indexable {

    String getId();

    int getLookupIndex();

    String getValue();

    String getDescription();

    String getTypeName();

    boolean isSuppressibleReservedWord();

    boolean isIdentifier();

    boolean isVariable();

    boolean isQuotedIdentifier();

    boolean isKeyword();

    boolean isFunction();

    boolean isParameter();

    boolean isDataType();

    boolean isLiteral();

    boolean isNumeric();

    boolean isCharacter();

    boolean isOperator();

    boolean isChameleon();

    boolean isReservedWord();

    boolean isParserLandmark();

    @NotNull
    TokenTypeCategory getCategory();

    TokenTypeBundleBase getBundle();

    @Nullable
    DBObjectType getObjectType();

    FormattingDefinition getFormatting();

    TokenPairTemplate getTokenPairTemplate();

    void setDefaultFormatting(FormattingDefinition defaults);

    boolean isOneOf(TokenType ... tokenTypes);

    boolean matches(TokenType tokenType);
}
