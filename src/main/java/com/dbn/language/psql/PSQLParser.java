package com.dbn.language.psql;

import com.dbn.language.common.DBLanguageDialect;
import com.dbn.language.common.DBLanguageParser;

public class PSQLParser extends DBLanguageParser {
    public PSQLParser(DBLanguageDialect languageDialect, String tokenTypesFile, String elementTypesFile, String defaultParseRootId) {
        super(languageDialect, tokenTypesFile, elementTypesFile, defaultParseRootId);
    }

/*
    @NotNull
    @Override
    public ASTNode parse(IElementType rootElementType, PsiBuilder builder, String parseRootId) {
        if (DatabaseNavigator.getInstance().isTempPSQLParsingEnabled()) {
            return super.parse(rootElementType, builder, parseRootId);
        } else {
            PsiBuilder.Marker marker = builder.mark();
            boolean advancedLexer = false;
            while (!builder.eof()) {
                builder.advanceLexer();
                advancedLexer = true;
            }
            if (!advancedLexer) builder.advanceLexer();
            marker.done(rootElementType);
            return builder.getTreeBuilt();
        }
    }
*/
}