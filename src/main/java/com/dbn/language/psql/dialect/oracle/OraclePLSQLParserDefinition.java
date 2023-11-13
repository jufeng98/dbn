package com.dbn.language.psql.dialect.oracle;

import com.dbn.language.psql.PSQLParserDefinition;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class OraclePLSQLParserDefinition extends PSQLParserDefinition {

    OraclePLSQLParserDefinition(OraclePLSQLParser parser) {
        super(parser);
    }

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new OraclePLSQLParserFlexLexer(getTokenTypes()));
    }
}