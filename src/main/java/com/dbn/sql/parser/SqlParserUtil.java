package com.dbn.sql.parser;

import com.dbn.sql.psi.SqlTypes;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.dbn.sql.psi.SqlTypes.MODULE_ARGUMENT;

/**
 * @author yudong
 */
public class SqlParserUtil extends GeneratedParserUtilBase {

    public static Boolean custom_module_argument(PsiBuilder builder, int level, Parser columnName) {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "module_argument")) {
            return false;
        }
        PsiBuilder.Marker marker = GeneratedParserUtilBase.enter_section_(
                builder,
                level,
                GeneratedParserUtilBase._COLLAPSE_,
                MODULE_ARGUMENT,
                "<module argument>"
        );
        columnName.parse(builder, level + 1);
        var parens = 0;
        while (builder.getTokenType() != SqlTypes.COMMA) {
            if (builder.getTokenType() == SqlTypes.LP) parens++;
            if (builder.getTokenType() == SqlTypes.RP && (--parens == -1)) break;
            builder.advanceLexer();
        }
        boolean result = (parens <= 0);
        GeneratedParserUtilBase.exit_section_(builder, level, marker, result, false, null);
        return result;
    }
}
