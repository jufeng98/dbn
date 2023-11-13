package com.dbn.language.psql.dialect.mysql;

import com.dbn.language.psql.PSQLParserDefinition;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class MysqlPSQLParserDefinition extends PSQLParserDefinition {
    MysqlPSQLParserDefinition(MysqlPSQLParser parser) {
        super(parser);
    }

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new MysqlPSQLParserFlexLexer(getTokenTypes()));
    }


}
